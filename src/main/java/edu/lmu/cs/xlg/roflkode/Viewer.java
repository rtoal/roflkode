package edu.lmu.cs.xlg.roflkode;

import static javax.swing.KeyStroke.getKeyStroke;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

import edu.lmu.cs.xlg.roflkode.entities.Entity;
import edu.lmu.cs.xlg.roflkode.entities.Script;
import edu.lmu.cs.xlg.roflkode.syntax.Parser;
import edu.lmu.cs.xlg.util.Log;

/**
 * A simple GUI application for viewing the different things the ROFLKODE
 * compiler can do.
 *
 * The application has two panes.  The left is a simple text editor in
 * which one can edit a ROFLKODE program, load one from the file system,
 * and save to the file system.  The right shows a view of the program
 * in response to a user selection action.  The user can choose to see
 * <ul>
 *   <li>The abstract syntax tree
 *   <li>The semantic graph
 *   <li>The squid
 *   <li>The generated assembly language
 *   <li>The executable file
 * </ul>
 */
@SuppressWarnings("serial")
public class Viewer extends JFrame {

    private JTextArea source = new JTextArea(30, 60);
    private JTextArea view = new JTextArea(30, 80) {
        public void setText(String text) {
            super.setText(text);
            this.setCaretPosition(0);
        }
    };
    private JScrollPane sourcePane = new JScrollPane();
    private JScrollPane viewPane = new JScrollPane();
    private StringWriter errors = new StringWriter();
    private Log log = new Log("roflkode", new PrintWriter(errors));
    private File currentFile;
    private JFileChooser chooser = new JFileChooser(".");

    /**
     * Creates new Viewer.
     */
    public Viewer() {
        JSplitPane splitPane = new JSplitPane();
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu();
        JMenu viewMenu = new JMenu();

        Action newAction = new AbstractAction("New") {
            {
                putValue(Action.MNEMONIC_KEY, KeyEvent.VK_N);
                putValue(Action.ACCELERATOR_KEY, getKeyStroke("control N"));
            }
            public void actionPerformed(ActionEvent e) {newFile();}
        };

        Action openAction = new AbstractAction("Open") {
            {
                putValue(Action.MNEMONIC_KEY, KeyEvent.VK_O);
                putValue(Action.ACCELERATOR_KEY, getKeyStroke("control O"));
            }
            public void actionPerformed(ActionEvent e) {openFile();}
        };

        Action saveAction = new AbstractAction("Save") {
            {
                putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);
                putValue(Action.ACCELERATOR_KEY, getKeyStroke("control S"));
            }
            public void actionPerformed(ActionEvent e) {saveFile();}
        };

        Action saveAsAction = new AbstractAction("Save As") {
            public void actionPerformed(ActionEvent e) {saveAsFile();}
        };

        Action quitAction = new AbstractAction("Quit") {
            {
                putValue(Action.MNEMONIC_KEY, KeyEvent.VK_Q);
                putValue(Action.ACCELERATOR_KEY, getKeyStroke("control Q"));
            }
            public void actionPerformed(ActionEvent e) {quit();}
        };

        Action syntaxAction = new AbstractAction("Syntax") {
            {
                putValue(Action.ACCELERATOR_KEY, getKeyStroke("F1"));
            }
            public void actionPerformed(ActionEvent e) {viewSyntaxTree();}
        };

//        Action semanticsAction = new AbstractAction("Semantics") {
//            {
//                putValue(Action.ACCELERATOR_KEY, getKeyStroke("F2"));
//            }
//            public void actionPerformed(ActionEvent e) {viewSemanticGraph();}
//        };
//
//        Action quadsAction = new AbstractAction("Quads") {
//            {
//                putValue(Action.ACCELERATOR_KEY, getKeyStroke("F3"));
//            }
//            public void actionPerformed(ActionEvent e) {viewQuads();}
//        };
//
//        Action optimizeAction = new AbstractAction("Optimize") {
//            {
//                putValue(Action.ACCELERATOR_KEY, getKeyStroke("F4"));
//            }
//            public void actionPerformed(ActionEvent e) {viewOptimizedQuads();}
//        };
//
//        Action assemblyAction = new AbstractAction("Assembly") {
//            {
//                putValue(Action.ACCELERATOR_KEY, getKeyStroke("F5"));
//            }
//            public void actionPerformed(ActionEvent e) {viewAssembly();}
//        };
//
//        Action executableAction = new AbstractAction("Executable") {
//            {
//                putValue(Action.ACCELERATOR_KEY, getKeyStroke("F11"));
//            }
//            public void actionPerformed(ActionEvent e) {viewExecutable();}
//        };

        fileMenu.setText("File");
        fileMenu.add(newAction);
        fileMenu.add(openAction);
        fileMenu.add(saveAction);
        fileMenu.add(saveAsAction);
        fileMenu.add(quitAction);
        menuBar.add(fileMenu);

        viewMenu.setText("View");
        viewMenu.add(syntaxAction);
//        viewMenu.add(semanticsAction);
//        viewMenu.add(quadsAction);
//        viewMenu.add(optimizeAction);
//        viewMenu.add(assemblyAction);
//        viewMenu.add(executableAction);
        menuBar.add(viewMenu);
        setJMenuBar(menuBar);

        source.setFont(new Font("Monospaced", 0, 16));
        sourcePane.setViewportView(source);
        splitPane.setLeftComponent(sourcePane);
        view.setEditable(false);
        view.setFont(new Font("Monospaced", 0, 16));
        viewPane.setViewportView(view);
        splitPane.setRightComponent(viewPane);
        splitPane.setDividerLocation(480);
        getContentPane().add(splitPane, BorderLayout.CENTER);

        setTitle("ROFLKODE Viewer");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1024, 712);
    }

    // Action implementations

    private void newFile() {
        currentFile = null;
        source.setText("");
    }

    private void openFile() {
        chooser.showOpenDialog(this);
        currentFile = chooser.getSelectedFile();
        if (currentFile == null) return;

        StringBuffer buffer = new StringBuffer();
        try {
            BufferedReader in = new BufferedReader(
                new FileReader(currentFile.getCanonicalPath()));
            String line;
            while ((line = in.readLine()) != null) {
                buffer.append(line).append("\n");
            }
            in.close();
        } catch (IOException ignored) {
        }
        source.setText(buffer.toString());
    }

    private void saveFile() {
        if (currentFile == null) {
            view.setText("Nothing to save");
            return;
        }

        try {
            BufferedWriter out = new BufferedWriter(
                new FileWriter(currentFile.getCanonicalPath()));
            out.write(source.getText());
            out.close();
            view.setText("File saved");
        } catch (IOException e) {
            view.setText("File not saved: " + e.getMessage());
        }
    }

    private void saveAsFile() {
        chooser.showSaveDialog(this);
        currentFile = chooser.getSelectedFile();
        if (currentFile != null) {
            saveFile();
        }
    }

    private void quit() {
        System.exit(0);
    }

    private void viewSyntaxTree() {
        viewPane.setViewportView(view);
        Script script = parse();
        if (log.getErrorCount() > 0) {
            view.setText(errors.toString());
        } else {
            try {
                viewPane.setViewportView(
                    new JTree(makeTree(script, "")));
            } catch (IllegalStateException e) {
                view.setText("Internal error: Syntax tree has cycles\n"
                    + e.getMessage());
            }
        }
    }

//    private void viewSemanticGraph() {
//        viewPane.setViewportView(view);
//        Program program = analyze();
//        if (log.getErrorCount() > 0) {
//            view.setText(errors.toString());
//        } else {
//            StringWriter writer = new StringWriter();
//            Entity.dump(new PrintWriter(writer), program);
//            view.setText(writer.toString());
//        }
//    }
//
//    private void viewQuads() {
//        viewPane.setViewportView(view);
//        UserSubroutine main = translate();
//        if (log.getErrorCount() > 0) {
//            view.setText(errors.toString());
//        } else {
//            StringWriter writer = new StringWriter();
//            main.dump(new PrintWriter(writer));
//            view.setText(writer.toString());
//        }
//    }
//
//    private void viewOptimizedQuads() {
//        viewPane.setViewportView(view);
//        UserSubroutine main = optimize();
//        if (log.getErrorCount() > 0) {
//            view.setText(errors.toString());
//        } else {
//            StringWriter writer = new StringWriter();
//            main.dump(new PrintWriter(writer, true));
//            view.setText(writer.toString());
//        }
//    }
//
//    private void viewAssembly() {
//        viewPane.setViewportView(view);
//        Writer assemblyWriter = assemble();
//        if (log.getErrorCount() > 0) {
//            view.setText(errors.toString());
//        } else {
//            view.setText(assemblyWriter.toString());
//        }
//    }
//
//    private void viewExecutable() {
//        viewPane.setViewportView(view);
//        view.setText("Executable view not yet implemented");
//    }

    // Wrappers for compiler operations

    private Script parse() {
        log.clearErrors();
        errors.getBuffer().setLength(0);
        Reader reader = new StringReader(source.getText());
        return new Parser(reader).parse(reader, log);
    }

//    private Program analyze() {
//        Program program = parse();
//        if (log.getErrorCount() > 0) return null;
//        program.analyze(log);
//        return program;
//    }
//
//    private UserSubroutine translate() {
//        Program program = analyze();
//        if (log.getErrorCount() > 0) return null;
//        return new CarlosToSquidTranslator(4, 8).translateProgram(program);
//    }
//
//    private UserSubroutine optimize() {
//        UserSubroutine main = translate();
//        if (log.getErrorCount() > 0) return null;
//        new Optimizer().optimizeSubroutine(main);
//        return main;
//    }
//
//    private Writer assemble() {
//        UserSubroutine main = optimize();
//        if (log.getErrorCount() > 0) return null;
//        Writer writer = new StringWriter();
//        SquidToNasmTranslator.generate(main, writer, "Carlos");
//        return writer;
//    }

    // Syntax tree builder

    /**
     * Returns a syntax tree for the hierarchy rooted at entity e.
     * Each node will have the name "prefix: classname" where prefix
     * is supplied as an argument to this method, and classname is
     * the name of this object's class.  The children of the node are
     * obtained from the non-static, non-null fields of this object.
     * Each of the fields which have type Entity will of course be
     * roots of subtrees; all other fields will generate leaf nodes.
     */
    public MutableTreeNode makeTree(Entity e, String prefix) {

        // Build a node for it with the prefix and the class name
        String className = e.getClass().getName();
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(
            prefix + ": " + className.substring(className.lastIndexOf('.') + 1)
        );

        // Add all the children
        for (Field field: relevantFields(e.getClass())) {
            try {
                field.setAccessible(true);
                String name = field.getName();
                Object value = field.get(e);
                if (value == null) continue;
                if ((field.getModifiers() & Modifier.STATIC) != 0) continue;

                if (value instanceof Entity) {
                    node.add(makeTree((Entity)value, name));
                } else if (value instanceof Collection<?>) {
                    try {
                        for (Object child: (Collection<?>)value) {
                            node.add(makeTree((Entity)child, name));
                        }
                    } catch (ClassCastException cce) {
                        node.setUserObject(node.getUserObject()
                                + "  " + name + "=\"" + value + "\"");
                    }
                } else {
                    // Simple attribute, attach description to node name
                    node.setUserObject(node.getUserObject()
                        + "  " + name + "=\"" + value + "\"");
                }
            } catch (IllegalAccessException cannotHappen) {
            }
        }
        return node;
    }

    /**
     * Returns a list of all non-private fields of class c, together
     * with fields of its ancestor classes, assuming that c is
     * a descendant class of Entity.
     */
    private static List<Field> relevantFields(Class<?> c) {
        ArrayList<Field> attributes = new ArrayList<Field>();
        attributes.addAll(Arrays.asList(c.getDeclaredFields()));
        if (c.getSuperclass() != Entity.class) {
            attributes.addAll(relevantFields(c.getSuperclass()));
        }
        return attributes;
    }

    /**
     * Runs the application.
     */
    public static void main(String args[]) {
        new Viewer().setVisible(true);
    }
}

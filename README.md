# Roflkode

Roflkode is an imperative, block structured, statically-typed programming language that is 
very similar to Adam Lindsay's language LOLCODE. It was designed during a compiler construction class,
with plenty of student input.

The language is designed to be implementable by small teams of undergraduate students in a 
single semester. Therefore it subsets LOLCODE in places; for example, it does not do interpolation 
within strings. In addition, in order to address some "interesting" aspects of compiler construction, 
it deviates from LOLCODE by adopting static typing, infix operators, operator precedence, and 
bracketing for array, bucket, and call expressions.

## Microsyntax

## Macrosyntax

```
  Script       →  br* 'HAI' br+ Import* Stmt+ KTHXBYE br*
  Import       →  'CAN' 'HAS' id '?' br+
  Stmt         →  (Dec | Simplestmt Modifier? | Complexstmt) br+
  Dec          →  Vardec | Typedec | Fundec
  Vardec       →  'I' 'HAS' 'A' Type? id ('ITZ' '4EVER'? Exp)?
  Type         →  'B00L' | 'KAR' | 'INT' | 'NUMBR' | 'YARN' | id | Type 'LIST'
  Typedec      →  'TEH' 'BUKKIT' 'UV' br* (Type id br*)* 'AKA' id
  Fundec       →  'I' 'CAN' ('MAEK' Type)? id Params? br+ Stmt+ 'SRSLY'
               |  'THEM' 'CAN' ('MAEK' Type)? id Params?
  Params       →  'WIF'? 'UR' Type id ('AN' Type id)*
  Simplestmt   →  'YO' Exp+
               |  'FACEPALM' Exp+
               |  'UPZORZ' Var
               |  'NERFZORZ' Var
               |  Var 'R' Exp
               |  'GTFO' id
               |  'HWGA' id?
               |  'HEREZ' 'UR' Exp
               |  'DIAF' Exp?
               |  'GIMMEH' Var
               |  'BRB' Exp
               |  id Args
  Modifier     →  ('IF' | 'CEPT' 'IF' | 'WHIEL' | 'TIL') Exp
  Complexstmt  →  Conditional
               |  Switch
               |  Loop
               |  Try
  Conditional  →  Exp '?' br+ 'WERD' br+ Stmt+ ('MEBBE' Exp br* Stmt+)* ('NO' 'WAI' br* Stmt+)? 'OIC'
  Switch       →  Exp 'WTF' '?' br+ ('OMG' literal br+ Stmt+)+ 'OMGWTF' br+ Stmt+ 'OIC'
  Loop         →  'IM' 'IN' 'UR' id Loopcontrol? br+ Stmt+ 'LOL'
  Loopcontrol  →  ('WHIEL' | 'TIL') Exp
               |  ('UPPIN' | 'NERFIN') id ('FROM' Exp 'TO' Exp | 'THRU' Exp)
  Try          →  'PLZ' Simplestmt br+ 'AWSUM' 'THX' br+ Stmt+ 'O' 'NOES' br+ Stmt+ 'MKAY'
  Exp          →  Exp1 (ORELSE Exp1)*
  Exp1         →  Exp2 (ANALSO Exp2)*
  Exp2         →  Exp3 (BITOR Exp)*
  Exp3         →  Exp4 (BITXOR Exp4)*
  Exp4         →  Exp5 (BITAND Exp5)*
  Exp5         →  Exp6 (relop Exp6)?
  Exp6         →  Exp7 (shiftop Exp7)*
  Exp7         →  Exp8 (addop Exp8)*
  Exp8         →  Exp9 (mulop Exp9)*
  Exp9         →  Prefixop? Exp10
  Exp10        →  Literal
               |  Var
               |  id '<:' Exp* ':>'
               |  '[:' Exp* ':]'
               |  '(' Exp* ')'
  Literal      →  'N00B'
               |  'WIN'
               |  'FAIL'
               |  intlit
               |  numlit
               |  charlit
               |  stringlit
  Var          →  id Args? ('!?' Exp '?!' | '!!!' id)*
  Args         →  '(:' Exp* ':)'
  Relop        →  'PWNS' | 'PWNED' 'BY' | 'SAEM' 'AS' | 'PWNS' 'OR' 'SAEM' 'AS' | 'PWNED' 'BY' 'OR' 'SAEM' 'AS' | 'DIVIDZ'
  Shiftop      →  'BITZLEFT' | 'BITZRIGHT'
  Addop        →  'UP' | 'NERF' | '~~'
  Mulop        →  'TIEMZ' | 'OVR' | 'LEFTOVR'
  Prefixop     →  'NAA' | 'BITZFLIP' | 'SIEZ' 'UV' | 'B00LZOR' | 'INTZOR' | 'NUMZOR' | 'KARZOR' | 'YARNZOR'

```

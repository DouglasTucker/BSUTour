#  Expected Interpreter Grammar Structure
## "Not case sensitive"


### PROGRAM 
<pre>
Program				:'PROGRAM' identifier {VAR_list} {Function_Block} {Function} StatementList 'END_PROGRAM'
</pre>

### VARS
<pre>
VAR_List			: VAR {VAR_List} 
VAR				: 'VAR'{RETAIN} DeclarationList 'END_VAR'
				| 'VAR_INPUT' {'RETAIN'} DeclarationList 'END_VAR'
				| 'VAR_OUTPUT' {'RETAIN'} DeclarationList 'END_VAR'
				| 'VAR_IN_OUT' {'RETAIN'} DeclarationList 'END_VAR'
				| 'VAR_EXTERNAL' {'RETAIN'} DeclarationList 'END_VAR'
				| 'VAR_GLOBAL' {'RETAIN'} DeclarationList 'END_VAR'
				
DeclarationList			: variable ':' type ';' {DeclarationList}
</pre>	


### Function_Block

### Function



### Expressions
<pre>
Expression			: XOR_Expression {'OR' XOR_Expression}
XOR_Expression			: AND_Expression {‘XOR’ AND_Expression}
AND_Expression			: Comparison { (‘&’ | ‘AND’) Comparison}
Comparision			: EquExpression { ( ‘=’ | ‘<>’) EquExpression}
EquExpression			: AddExpression {ComparisonOperator AddExpression}
ComparisionOperator		: ‘<’
				| ‘>’
				| ‘<=’
				| ‘>=’
AddExpression			: Term {AddOperator Term}
AddOperator			: ‘+’
				| ‘-’
Term				: PowerExpression {MultiplyOperator PowerExpresion}
MultiplyOperator		: ‘*’
				| ’/’
				| ‘MOD’
PowerExpression			: UnaryExpression {‘**’ UnaryExpression}
UnaryExpression			: [UnaryOperator] PrimaryExpression
UnaryOperator			: ‘-‘
				| ‘NOT’
PrimaryExpression		: Constant
				| Variable
				| ‘(‘ Expression ‘)’
				| FunctionName ‘(‘ [ST_FunctionInputs] ‘)’
ST_FunctionInputs		: ST_FunctionInput { ‘,’ ST_FunctionInput}
ST_FunctionInput		: [VariableName ‘:=’] Expression
</pre>

### Statements
<pre>
StatementList			: Statement ';' {Statement ';'}
Statement			: NIL
				| Assignment Statement
				| SubprogramControlStatement
				| SelectionStatement
				| IterationStatement
</pre>


### Assignment Statements
<pre>
AssignmentStatement		: Variable ':=' Expression
</pre>


### Subprogram Control Statements
<pre>
SubprogramControlStatements	: FB_Invocation
				| ‘Return’
FB_Invocation			: FBName ‘(’ [FB_InputAssignment { ‘,’ FB_InputAssignent}] ‘)’
FB_InputAssignment		: VariableName ‘:=’ Expression
</pre>


### Selection statements
<pre>
SelectionStatement		: IfStatement
				| CaseStatement
IfStatement			: ‘IF’ Expression ‘THEN’ StatementList 
				   { ‘ElSIF’ Expression ‘THEN' StatementList } 
				   [‘Else’ StatementList] ‘END_IF’

CaseStatement			: ‘CASE’ Expression ‘OF’ CaseElement
				   {CaseElement}
				   [‘ELSE’ StatementList]
				   ‘END_CASE’

CaseElement			: CaseList ‘:’ StatementList
CaseList			: CaseListElement { ‘,’ CaseListElement}
CaseListElement			: Subrange | SignedInteger
</pre>


### Iteration Statements
<pre>
IterationStatement		: ForStatement
				| WhileStatement
				| RepeatStatement
				| ExitStatement
ForStatement			: ‘FOR’ ControlVariable ‘:=’ ForList ‘DO’
				   StementList
				   ‘END_FOR’

ControlVariable			: Identifier
ForList				: Expression ‘TO’ Expression
				   [ ‘BY’ Expression]

WhileStatement			: ‘WHILE’ Expression ‘DO’
				   StatementList
				   ‘END_WHILE’

RepeatStatement			: ‘REPEAT’
				   StatementList
				   ‘UNTIL’ EXPRESSION
				   ‘END_REPEAT’

ExitStatment			: ‘EXIT’

</pre>

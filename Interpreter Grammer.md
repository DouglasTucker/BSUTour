#  Expected Interpreter Grammar Structure
## "Not case sensitive"


### PROGRAM
-

### Expressions
<pre>
Expression                      : XOR_Expression {'OR' XOR_Expression}
XOR_Expression             : AND_Expression {‘XOR’ AND_Expression}
AND_Expression             : Comparison { (‘&’ | ‘AND’) Comparison}
Comparision                    : EquExpression { ( ‘=’ | ‘<>’) EquExpression}
EquExpression                : AddExpression {ComparisonOperator AddExpression}
ComparisionOperator        : ‘<’
                | ‘>’
                | ‘<=’
                | ‘>=’
AddExpression            : Term {AddOperator Term}
AddOperator            : ‘+’
                | ‘-’
Term                : PowerExpression {MultiplyOperator PowerExpresion}
MultiplyOperator        : ‘*’
                | ’/’
                | ‘MOD’
PowerExpression        : UnaryExpression {‘**’ UnaryExpression}
UnaryExpression        : [UnaryOperator] PrimaryExpression
UnaryOperator        : ‘-‘
                | ‘NOT’
PrimaryExpression        : Constant
                | Variable
                | ‘(‘ Expression ‘)’
                | FunctionName ‘(‘ [ST_FunctionInputs] ‘)’
ST_FunctionInputs        : ST_FunctionInput { ‘,’ ST_FunctionInput}
ST_FunctionInput        : [VariableName ‘:=’] Expression

</pre>

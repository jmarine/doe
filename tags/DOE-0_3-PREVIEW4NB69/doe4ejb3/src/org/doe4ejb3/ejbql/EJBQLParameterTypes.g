/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the "License").  You may not use this file except
 * in compliance with the License.
 *
 * You can obtain a copy of the license at
 * glassfish/bootstrap/legal/CDDLv1.0.txt or
 * https://glassfish.dev.java.net/public/CDDLv1.0.html.
 * See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL
 * HEADER in each file and include the License file at
 * glassfish/bootstrap/legal/CDDLv1.0.txt.  If applicable,
 * add the following below this CDDL HEADER, with the
 * fields enclosed by brackets "[]" replaced with your
 * own identifying information: Portions Copyright [yyyy]
 * [name of copyright owner]
 */
// Experimental grammar to get parameters information from EJBQL statements.
// This code is based on "EJBQLParser.g" from Oracle TopLink-Essentials
// (Copyright (c) 1998, 2006, Oracle. All rights reserved.)
// (Added 20/12/2000 JED. Define the package for the class)
// (Modified 24/09/2006 <jmarine@dev.java.net>. Get parameter information from EJBQL statement)
header {
    package org.doe4ejb3.ejbql;

    import java.lang.reflect.Field;
    import java.lang.reflect.Method;
    import java.lang.reflect.Member;

    import java.sql.Date;
    import java.sql.Time;
    import java.sql.Timestamp;
    import java.util.List;
    import java.util.ArrayList;
    import java.util.HashMap;
}

/** */
class EJBQLParameterTypesParser extends Parser;
options {
    exportVocab=EJBQL;
    k = 3; // This is the number of tokens to look ahead to
    buildAST = false;
}

tokens {
    ABS="abs";
    ALL="all";
    AND="and";
    ANY="any";
    AS="as";
    ASC="asc";
    AVG="avg";
    BETWEEN="between";
    BOTH="both";
    BY="by";
    CONCAT="concat";
    COUNT="count";
    CURRENT_DATE="current_date";
    CURRENT_TIME="current_time";
    CURRENT_TIMESTAMP="current_timestamp";
    DESC="desc";
    DELETE="delete";
    DISTINCT="distinct";
    EMPTY="empty";
    ESCAPE="escape";
    EXISTS="exists";
    FALSE="false";
    FETCH="fetch";
    FROM="from";
    GROUP="group";
    HAVING="having";
    IN="in";
    INNER="inner";
    IS="is";
    JOIN="join";
    LEADING="leading";
    LEFT="left";
    LENGTH="length";
    LIKE="like";
    LOCATE="locate";
    LOWER="lower";
    MAX="max";
    MEMBER="member";
    MIN="min";
    MOD="mod";
    NEW="new";
    NOT="not";
    NULL="null";
    OBJECT="object";
    OF="of";
    OR="or";
    ORDER="order";
    OUTER="outer";
    SELECT="select";
    SET="set";
    SIZE="size";
    SQRT="sqrt";
    SOME="some";
    SUBSTRING="substring";
    SUM="sum";
    TRAILING="trailing";
    TRIM="trim";
    TRUE="true";
    UNKNOWN="unknown";
    UPDATE="update";
    UPPER="upper";
    WHERE="where";
}

{
    /** entity alias in from clause */
    HashMap entityClassByAliasName = new HashMap();

    /** input parameter types */
    HashMap paramTypesByName = new HashMap();


    /** Flag indicating whether aggregates are allowed. */
    boolean aggregatesAllowed = false;

    /** */
    protected void setAggregatesAllowed(boolean allowed) {
        this.aggregatesAllowed = allowed;
    }

    /** */
    protected boolean aggregatesAllowed() {
        return aggregatesAllowed;
    }


    /** */
    public HashMap getEntityClassesByAliasName() {
        return entityClassByAliasName;
    }

    public HashMap getParameterTypesByName() {
        return paramTypesByName;
    }



    protected Class getClassFromEntityName(String entityName)
    {
        throw new RuntimeException("The method 'getClassFromEntityName' should be overriden.");
    }


    protected Class getMemberType(Class variableClass, String attributeName)
    {
	Member member = null;
	try { 
           member = variableClass.getMethod("get" + capitalize(attributeName)); 
        } catch(Exception ex1) { 
           try { member = variableClass.getMethod("is" + capitalize(attributeName)); }
           catch(Exception ex2) { 
             try { member = variableClass.getField(attributeName); }
             catch(Exception ex3) { System.out.println("Attribute '" + attributeName + "' not found for class '" + variableClass + "'."); }
           }
        } 

	if(member != null) {
          if(member instanceof Method) {
  	    Method method = (Method)member;
  	    return method.getReturnType(); 
          } else if(member instanceof Field) {
	     Field field = (Field)member;
	     return field.getType();
          }
        }

	throw new RuntimeException("Attribute '" + attributeName + "' not found for class '" + variableClass + "'.");
    }

    protected String capitalize(String name)
    {
	if( (name == null) || (name.length() == 0) || Character.isUpperCase(name.charAt(0)) ) {
          return name;
        } else {
	  return name.substring(0,1).toUpperCase() + name.substring(1);
        }
    }

}

parseEJBQLParameterTypes returns [HashMap retval]
{ 
  retval = null; 
  entityClassByAliasName = new HashMap();
  paramTypesByName = new HashMap();

}
    : selectStatement { retval = paramTypesByName; }
    ;

selectStatement 
{ 
    aggregatesAllowed = false;
}
    : selectClause
      fromClause
      (whereClause)?
      (groupByClause)?
      (havingClause)?
      (orderByClause)?
      EOF 
    ;


//================================================

selectClause
    : t:SELECT (DISTINCT)?  selectExpression ( COMMA selectExpression  )*
    ;

selectExpression 
{ Class variableClass; }
    : variableClass = singleValuedPathExpression
    | variableClass = aggregateExpression
    | variableClass = variableAccess
    | OBJECT LEFT_ROUND_BRACKET variableClass = variableAccess RIGHT_ROUND_BRACKET
    | constructorExpression
    ;

aggregateExpression returns [Class retval]
{ 
  Class variableClass = null; 
  retval = null;
}
    : t1:AVG LEFT_ROUND_BRACKET (DISTINCT)? variableClass = stateFieldPathExpression RIGHT_ROUND_BRACKET { retval = variableClass; }
    | t2:MAX LEFT_ROUND_BRACKET (DISTINCT)? variableClass = stateFieldPathExpression RIGHT_ROUND_BRACKET { retval = variableClass; }
    | t3:MIN LEFT_ROUND_BRACKET (DISTINCT)? variableClass = stateFieldPathExpression RIGHT_ROUND_BRACKET { retval = variableClass; }
    | t4:SUM LEFT_ROUND_BRACKET (DISTINCT)? variableClass = stateFieldPathExpression RIGHT_ROUND_BRACKET { retval = variableClass; }
    | t5:COUNT LEFT_ROUND_BRACKET (DISTINCT)? ( variableClass = variableAccess | variableClass = pathExpression ) RIGHT_ROUND_BRACKET { retval = Integer.TYPE; }
    ;

constructorExpression 
    : t:NEW constructorName LEFT_ROUND_BRACKET constructorItem (COMMA constructorItem)* RIGHT_ROUND_BRACKET
    ;

constructorName 
    : i1:IDENT ( DOT i2:IDENT )*
    ;

constructorItem
{ Class variableClass = null; }
    : variableClass = singleValuedPathExpression
    | variableClass = aggregateExpression
    ;

fromClause
    : t:FROM identificationVariableDeclaration
        (COMMA  ( identificationVariableDeclaration | collectionMemberDeclaration) )*
    ;

identificationVariableDeclaration
    : rangeVariableDeclaration ( join )*
    ;

rangeVariableDeclaration 
{ 
    String schema;
}
    : schema = abstractSchemaName (AS)? i:IDENT
        { 
	    String alias = i.getText();
            if(alias == null) alias = schema;
	    entityClassByAliasName.put(alias, getClassFromEntityName(schema));
        }
    ;

// Non-terminal abstractSchemaName first matches any token to allow abstract 
// schema names that are keywords (such as order, etc.). 
// Method validateAbstractSchemaName throws an exception if the text of the 
// token is not a valid Java identifier.
abstractSchemaName returns [String schema]
{ schema = null; }
    : ident:. 
        {
            schema = ident.getText();
        }
    ;

join 
{
    Class joinClass = null;
}
    : joinSpec
      ( joinClass = joinAssociationPathExpression (AS)? i:IDENT
        {
            String alias = i.getText();
            if(alias == null) alias = joinClass.getName();
	    entityClassByAliasName.put(alias, joinClass);
        }
      | t:FETCH joinClass = joinAssociationPathExpression
        { 
            String alias = t.getText();
            if(alias == null) alias = joinClass.getName();
	    entityClassByAliasName.put(alias, joinClass);
        }
      )
    ;

joinSpec
    : (LEFT (OUTER)?  | INNER  )? JOIN
    ;

collectionMemberDeclaration
{ Class collectionClass = null; }
    : t:IN LEFT_ROUND_BRACKET collectionClass = collectionValuedPathExpression RIGHT_ROUND_BRACKET (AS)? i:IDENT
      {
	if(collectionClass != null) { 
            String alias = i.getText();
            if(alias == null) alias = collectionClass.getName();
            entityClassByAliasName.put(alias, collectionClass);
        }
      }
    ;

collectionValuedPathExpression returns [Class retval]
{ retval = null; }
    : retval = pathExpression
    ;

associationPathExpression returns [Class retval]
{ retval = null; }
    : retval = pathExpression
    ;

joinAssociationPathExpression returns [Class retval] 
{
    Class variableClass = null; 
    String attributeName = null;
    retval = null;
}
    : variableClass = variableAccess d:DOT attributeName = attribute
        { retval = getMemberType(variableClass, attributeName); }
    ;

singleValuedPathExpression returns [Class retval]
{ retval = null; }
    : retval = pathExpression
    ;

stateFieldPathExpression returns [Class retval]
{ retval = null; }
    : retval = pathExpression
    ;

pathExpression returns [Class variableClass]
{ 
    String attributeName = null;
    variableClass = null; 
}
    : variableClass = variableAccess
        (d:DOT attributeName = attribute
	    { variableClass = getMemberType(variableClass, attributeName); }
        )+
    ;

// Non-terminal attribute first matches any token to allow abstract 
// schema names that are keywords (such as order, etc.). 
// Method validateAttributeName throws an exception if the text of the 
// token is not a valid Java identifier.
attribute returns [String attributeName]
{ attributeName = null; }

    : i:.
        { 
	    attributeName = i.getText();
        }
    ;

variableAccess returns [Class variableClass]
{ 
  variableClass = null; 
  String variableName = null;
}
    : i:IDENT
        { 
	  variableName = i.getText();
	  variableClass = (Class)entityClassByAliasName.get(variableName);
        }
    ;

whereClause  
    : t:WHERE conditionalExpression
    ;

conditionalExpression
    : conditionalTerm
        (t:OR conditionalTerm)*
    ;

conditionalTerm
    : conditionalFactor (t:AND conditionalFactor)*
    ;

conditionalFactor 
    : (n:NOT)? 
        ( conditionalPrimary | existsExpression[(n!=null)] 
        )
    ;

conditionalPrimary
    : (LEFT_ROUND_BRACKET conditionalExpression) =>
        LEFT_ROUND_BRACKET conditionalExpression RIGHT_ROUND_BRACKET
    | simpleConditionalExpression
    ;

simpleConditionalExpression 
{
	Class leftClass = null;
}
    : leftClass = arithmeticExpression[null]
        simpleConditionalExpressionRemainder[leftClass]
    ;

simpleConditionalExpressionRemainder [Class leftClass]
    : comparisonExpression[leftClass]
    | ((NOT)? BETWEEN) => betweenExpression[leftClass]
    | ((NOT)? LIKE) => likeExpression[leftClass]
    | ((NOT)? IN) => inExpression[leftClass]
    | (IS (NOT)? NULL) => nullComparisonExpression
    | emptyCollectionComparisonExpression
    | collectionMemberExpression[leftClass]
    ;

betweenExpression [Class leftClass]
{
  Class exprClass = null;
}
    : ( n:NOT )? t:BETWEEN 
        exprClass = arithmeticExpression[leftClass] AND exprClass = arithmeticExpression[leftClass]
    ;

inExpression [Class leftClass]
    : ( n:NOT )? t:IN 
        LEFT_ROUND_BRACKET
        ( inItem[leftClass] ( COMMA inItem[leftClass] )*
        | subquery
        )
        RIGHT_ROUND_BRACKET
    ;

inItem [Class leftClass]
{ Class itemClass = null; }
    : literalString
    | itemClass = literalNumeric
    | inputParameter[leftClass]
    ;

likeExpression [Class leftClass]
    : ( n:NOT )? t:LIKE likeValue[leftClass]
        (escape[leftClass])?
    ;

escape [Class leftClass]
    : t:ESCAPE likeValue[leftClass]
    ;

likeValue [Class leftClass]
    : literalString
    | inputParameter[String.class]
    ;

nullComparisonExpression 
    : t:IS ( n:NOT )? NULL
    ;

emptyCollectionComparisonExpression
    : t:IS ( n:NOT )? EMPTY
    ;

collectionMemberExpression [Class leftClass]
{ Class exprClass = null; }
    : ( n:NOT )? t:MEMBER (OF)? exprClass = collectionValuedPathExpression
    ;

existsExpression [boolean not]
    : t:EXISTS LEFT_ROUND_BRACKET subquery RIGHT_ROUND_BRACKET
    ;

comparisonExpression [Class leftClass]
    : t1:EQUALS comparisonExpressionRightOperand[leftClass]
    | t2:NOT_EQUAL_TO comparisonExpressionRightOperand[leftClass] 
    | t3:GREATER_THAN comparisonExpressionRightOperand[leftClass]
    | t4:GREATER_THAN_EQUAL_TO comparisonExpressionRightOperand[leftClass]
    | t5:LESS_THAN comparisonExpressionRightOperand[leftClass]
    | t6:LESS_THAN_EQUAL_TO comparisonExpressionRightOperand[leftClass]
    ;

comparisonExpressionRightOperand [Class leftClass]
{
  Class exprClass = null;
}
    : exprClass = arithmeticExpression[leftClass]
    | anyOrAllExpression[leftClass]
    ;

arithmeticExpression [Class leftClass] returns [Class retval]
{
  Class exprClass = null;
  retval = null;
}
    : exprClass = simpleArithmeticExpression[leftClass] { retval = exprClass; }
    | LEFT_ROUND_BRACKET subquery RIGHT_ROUND_BRACKET
    ;

simpleArithmeticExpression [Class leftClass] returns [Class retval]
{
  Class exprClass = null;
  retval = null;
  if(leftClass == null) leftClass = Integer.TYPE;
}
    : exprClass = arithmeticTerm[leftClass] { retval = exprClass; }
        ( p:PLUS exprClass = arithmeticTerm[exprClass] { retval = exprClass; }
        | m:MINUS exprClass = arithmeticTerm[exprClass] { retval = exprClass; }
        )* 
    ;

arithmeticTerm [Class leftClass] returns [Class retval]
{ 
  Class exprClass = null;
  retval = null;
}
    : exprClass = arithmeticFactor[leftClass] { retval = exprClass; }
        ( m:MULTIPLY exprClass = arithmeticFactor[exprClass] { retval = exprClass; }
        | d:DIVIDE exprClass= arithmeticFactor[exprClass] { retval = exprClass; }
        )* 
    ;

arithmeticFactor [Class leftClass] returns [Class retval]
{ 
  Class exprClass = null;
  retval = null; 
}
    : p:PLUS  exprClass = arithmeticPrimary[leftClass] { retval = exprClass; }
    | m:MINUS exprClass = arithmeticPrimary[leftClass] { retval = exprClass; }
    | exprClass = arithmeticPrimary[leftClass] { retval = exprClass; }
    ;

arithmeticPrimary [Class leftClass] returns [Class retval]
{ 
  Class variableClass = null; 
  retval = null; 
}
    : { aggregatesAllowed() }? variableClass = aggregateExpression { retval = variableClass; }
    | variableClass = variableAccess { retval = variableClass; }
    | variableClass = stateFieldPathExpression { retval = variableClass; }
    | variableClass = functionsReturningNumerics { retval = variableClass; }
    | variableClass = functionsReturningDatetime { retval = variableClass; }
    | functionsReturningStrings { retval = String.class; }
    | inputParameter[leftClass] { retval = leftClass; }
    | variableClass = literalNumeric { retval = variableClass; }
    | literalString { retval = String.class; }
    | literalBoolean { retval = Boolean.TYPE; }
    | LEFT_ROUND_BRACKET variableClass = simpleArithmeticExpression[leftClass] RIGHT_ROUND_BRACKET { retval = variableClass; }
    ;

anyOrAllExpression [Class leftClass]
    : a:ALL LEFT_ROUND_BRACKET subquery RIGHT_ROUND_BRACKET
    | y:ANY LEFT_ROUND_BRACKET subquery RIGHT_ROUND_BRACKET
    | s:SOME LEFT_ROUND_BRACKET subquery RIGHT_ROUND_BRACKET
    ;

stringPrimary 
{ Class variableClass = null; }
    : literalString
    | functionsReturningStrings
    | inputParameter[String.class]
    | variableClass = stateFieldPathExpression
    ;

// Literals and Low level stuff

literal returns [Class retval]
{ 
  Class literalClass = null;
  retval = null; 
}
    : literalClass = literalNumeric { retval = literalClass; }
    | literalBoolean { retval = Boolean.TYPE; }
    | literalString { retval = String.class; }
    ;

literalNumeric returns [Class retval]
{ retval = null; }
    : i:NUM_INT { retval = Integer.TYPE; }
    | l:NUM_LONG { retval = Long.TYPE; }
    | f:NUM_FLOAT { retval = Float.TYPE; }
    | d:NUM_DOUBLE { retval = Double.TYPE; }
    ;

literalBoolean
    : t:TRUE  
    | f:FALSE 
    ;

literalString 
    : d:STRING_LITERAL_DOUBLE_QUOTED 
    | s:STRING_LITERAL_SINGLE_QUOTED
    ;

inputParameter [Class leftClass]
    : p:POSITIONAL_PARAM
        { 
            // skip the leading ?
            String text = p.getText().substring(1);
            paramTypesByName.put(text, leftClass);
        }
    | n:NAMED_PARAM
        { 
            // skip the leading :
            String text = n.getText().substring(1);
            paramTypesByName.put(text, leftClass);
        }
    ;

functionsReturningNumerics returns [Class retval]
{  retval = null; 
   Class functionClass = null;
}
    : functionClass = abs { retval = functionClass; }
    | length { retval = Integer.TYPE; }
    | mod { retval = Integer.TYPE; }
    | functionClass = sqrt { retval = functionClass; }
    | locate { retval = Integer.TYPE; }
    | size { retval = Integer.TYPE; }
    ;

functionsReturningDatetime returns [Class retval]
{ retval = null; }
    : d:CURRENT_DATE { retval = java.sql.Date.class; }
    | t:CURRENT_TIME { retval = java.sql.Time.class; }
    | ts:CURRENT_TIMESTAMP { retval = java.sql.Timestamp.class; }
    ;

functionsReturningStrings 
    : concat 
    | substring 
    | trim 
    | upper 
    | lower 
    ;

// Functions returning strings
concat 
    : c:CONCAT 
        LEFT_ROUND_BRACKET 
        stringPrimary COMMA stringPrimary
        RIGHT_ROUND_BRACKET
    ;

substring
{ Class exprClass = null; }
    : s:SUBSTRING   
        LEFT_ROUND_BRACKET
        stringPrimary COMMA
        exprClass = simpleArithmeticExpression[Integer.TYPE] COMMA
        exprClass = simpleArithmeticExpression[Integer.TYPE]
        RIGHT_ROUND_BRACKET
    ;

trim
    : t:TRIM
        LEFT_ROUND_BRACKET 
        ( ( trimSpec trimChar FROM )=> trimSpec trimChar FROM )? 
        stringPrimary
        RIGHT_ROUND_BRACKET
    ;

trimSpec 
    : LEADING
    | TRAILING
    | BOTH
    | // empty rule
    ;

trimChar 
    : literalString
    | inputParameter[String.class]
    | // empty rule
    ;

upper 
    : u:UPPER LEFT_ROUND_BRACKET stringPrimary RIGHT_ROUND_BRACKET
    ;

lower
    : l:LOWER LEFT_ROUND_BRACKET stringPrimary RIGHT_ROUND_BRACKET
    ;

// Functions returning numerics
abs returns [Class retval]
{ retval = null; }
    : a:ABS LEFT_ROUND_BRACKET retval = simpleArithmeticExpression[null] RIGHT_ROUND_BRACKET
    ;

length 
    : l:LENGTH LEFT_ROUND_BRACKET stringPrimary RIGHT_ROUND_BRACKET
    ;

locate
{ Class exprClass = null; }
    : l:LOCATE
        LEFT_ROUND_BRACKET 
        stringPrimary COMMA stringPrimary
        ( COMMA exprClass = simpleArithmeticExpression[Integer.TYPE] )?
        RIGHT_ROUND_BRACKET
    ;

size
{ Class exprClass = null; }
    : s:SIZE LEFT_ROUND_BRACKET exprClass = collectionValuedPathExpression RIGHT_ROUND_BRACKET
    ;

mod
{ Class exprClass = null; }
    : m:MOD LEFT_ROUND_BRACKET exprClass = simpleArithmeticExpression[null] COMMA exprClass = simpleArithmeticExpression[null] RIGHT_ROUND_BRACKET
    ;

sqrt returns [Class retval]
{ retval = null; }
    : s:SQRT LEFT_ROUND_BRACKET retval = simpleArithmeticExpression[null] RIGHT_ROUND_BRACKET
    ;

subquery 
    : simpleSelectClause
      subqueryFromClause
      (whereClause)?
      (groupByClause)?
      (havingClause)?
    ;

simpleSelectClause
    : s:SELECT (DISTINCT)? simpleSelectExpression
    ;

simpleSelectExpression
{ Class variableClass; } 
    : variableClass = singleValuedPathExpression
    | variableClass = aggregateExpression
    | variableClass = variableAccess
    ;

subqueryFromClause
    : f:FROM subselectIdentificationVariableDeclaration 
        ( COMMA subselectIdentificationVariableDeclaration )*
    ;

subselectIdentificationVariableDeclaration 
{
  Class variableClass = null;
}
    : identificationVariableDeclaration
    | variableClass = associationPathExpression (AS)? i:IDENT
      {     
        String alias = i.getText();
        if(alias == null) alias = variableClass.getName();
        entityClassByAliasName.put(alias, variableClass);
      }
    | collectionMemberDeclaration 
    ;

orderByClause 
    : o:ORDER BY orderByItem (COMMA orderByItem )*
    ; 

orderByItem 
{ Class variableClass = null; }
    : variableClass = stateFieldPathExpression
        ( a:ASC 
        | d:DESC
        | // empty rule
        )
    ;

groupByClause 
    : g:GROUP BY groupByItem (COMMA groupByItem )*
    ;

groupByItem 
{ Class variableClass; }
    : variableClass = stateFieldPathExpression
    | variableClass = variableAccess
    ;

havingClause
    : h:HAVING { setAggregatesAllowed(true); } 
        conditionalExpression 
        { 
            setAggregatesAllowed(false); 
        }
    ;

/** */
class EJBQLParameterTypesLexer extends Lexer;
options {   
    caseSensitive=false;
    caseSensitiveLiterals=false;
    k = 4;
    exportVocab=EJBQL;
    charVocabulary = '\u0000'..'\uFFFE';
}

// hexadecimal digit (again, note it's protected!)
protected
HEX_DIGIT
    :   ('0'..'9'|'a'..'f')
    ;

WS  : (' ' | '\t' | '\n' | '\r')+
    { $setType(Token.SKIP); } ;

LEFT_ROUND_BRACKET
    : '('
    ;

RIGHT_ROUND_BRACKET
    : ')'
    ;

COMMA
    : ','
    ;

IDENT 
    : TEXTCHAR
    ;

protected
TEXTCHAR
    : ('a'..'z' | '_' | '$' | 
       c1:'\u0080'..'\uFFFE' 
       {
           if (!Character.isJavaIdentifierStart(c1)) {
               throw new NoViableAltForCharException(
                   c1, getFilename(), getLine(), getColumn());
           }
       }
      )
      ('a'..'z' | '_' | '$' | '0'..'9' | 
       c2:'\u0080'..'\uFFFE'
       {
           if (!Character.isJavaIdentifierPart(c2)) {
               throw new NoViableAltForCharException(
                   c2, getFilename(), getLine(), getColumn());
           }
       }
      )*
    ;

// a numeric literal
NUM_INT
{ 
    boolean isDecimal=false; 
    int tokenType = NUM_DOUBLE;
}
    :   '.' {_ttype = DOT;}
            (('0'..'9')+ { tokenType = NUM_DOUBLE; } (EXPONENT)? (tokenType = FLOAT_SUFFIX)?
            {_ttype = tokenType; })?
    |   (   '0' {isDecimal = true;} // special case for just '0'
            (   ('x')
                (                                           // hex
                    // the 'e'|'E' and float suffix stuff look
                    // like hex digits, hence the (...)+ doesn't
                    // know when to stop: ambig.  ANTLR resolves
                    // it correctly by matching immediately.  It
                    // is therefor ok to hush warning.
                    options {
                        warnWhenFollowAmbig=false;
                    }
                :   HEX_DIGIT
                )+
            |   ('0'..'7')+                                 // octal
            )?
        |   ('1'..'9') ('0'..'9')*  {isDecimal=true;}       // non-zero decimal
        )
        (   ('l') { _ttype = NUM_LONG; }
        
        // only check to see if it's a float if looks like decimal so far
        |   {isDecimal}?
            { tokenType = NUM_DOUBLE; }
            (   '.' ('0'..'9')* (EXPONENT)? (tokenType = FLOAT_SUFFIX)?
            |   EXPONENT (tokenType = FLOAT_SUFFIX)?
            |   tokenType = FLOAT_SUFFIX
            )
            { _ttype = tokenType; }
        )?
    ;

// a couple protected methods to assist in matching floating point numbers
protected
EXPONENT
    :   ('e') ('+'|'-')? ('0'..'9')+
    ;


protected
FLOAT_SUFFIX returns [int tokenType]
{ tokenType = NUM_DOUBLE; }
    :   'f' { tokenType = NUM_FLOAT; }
    |   'd' { tokenType = NUM_DOUBLE; }
    ;

EQUALS
    : '='
    ;

GREATER_THAN
    : '>'
    ;

GREATER_THAN_EQUAL_TO
    : ">="
    ;

LESS_THAN
    : '<'
    ;

LESS_THAN_EQUAL_TO
    : "<="
    ;

NOT_EQUAL_TO
    : "<>"
    ;

MULTIPLY
    : "*"
    ;

DIVIDE
    : "/"
    ;

PLUS
    : "+"
    ;

MINUS
    : "-"
    ;

POSITIONAL_PARAM
    : "?" ('1'..'9') ('0'..'9')*
    ;

NAMED_PARAM
    : ":" TEXTCHAR
    ;

// Added Jan 9, 2001 JED
// string literals
STRING_LITERAL_DOUBLE_QUOTED
    : '"' (~ ('"'))* '"'
    ;

STRING_LITERAL_SINGLE_QUOTED
    : '\'' (~ ('\'') | ("''"))* '\'' 
    ;

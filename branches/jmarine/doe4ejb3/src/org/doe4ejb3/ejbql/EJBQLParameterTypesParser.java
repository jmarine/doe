// $ANTLR 2.7.6 (20060904): "EJBQLParameterTypes.g" -> "EJBQLParameterTypesParser.java"$

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

import antlr.TokenBuffer;
import antlr.TokenStreamException;
import antlr.TokenStreamIOException;
import antlr.ANTLRException;
import antlr.LLkParser;
import antlr.Token;
import antlr.TokenStream;
import antlr.RecognitionException;
import antlr.NoViableAltException;
import antlr.MismatchedTokenException;
import antlr.SemanticException;
import antlr.ParserSharedInputState;
import antlr.collections.impl.BitSet;

/** */
public class EJBQLParameterTypesParser extends antlr.LLkParser       implements EJBQLTokenTypes
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


protected EJBQLParameterTypesParser(TokenBuffer tokenBuf, int k) {
  super(tokenBuf,k);
  tokenNames = _tokenNames;
}

public EJBQLParameterTypesParser(TokenBuffer tokenBuf) {
  this(tokenBuf,3);
}

protected EJBQLParameterTypesParser(TokenStream lexer, int k) {
  super(lexer,k);
  tokenNames = _tokenNames;
}

public EJBQLParameterTypesParser(TokenStream lexer) {
  this(lexer,3);
}

public EJBQLParameterTypesParser(ParserSharedInputState state) {
  super(state,3);
  tokenNames = _tokenNames;
}

	public final HashMap  parseEJBQLParameterTypes() throws RecognitionException, TokenStreamException {
		HashMap retval;
		
		
		retval = null; 
		entityClassByAliasName = new HashMap();
		paramTypesByName = new HashMap();
		
		
		
		try {      // for error handling
			selectStatement();
			if ( inputState.guessing==0 ) {
				retval = paramTypesByName;
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_0);
			} else {
			  throw ex;
			}
		}
		return retval;
	}
	
	public final void selectStatement() throws RecognitionException, TokenStreamException {
		
		
		aggregatesAllowed = false;
		
		
		try {      // for error handling
			selectClause();
			fromClause();
			{
			switch ( LA(1)) {
			case WHERE:
			{
				whereClause();
				break;
			}
			case EOF:
			case GROUP:
			case HAVING:
			case ORDER:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			switch ( LA(1)) {
			case GROUP:
			{
				groupByClause();
				break;
			}
			case EOF:
			case HAVING:
			case ORDER:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			switch ( LA(1)) {
			case HAVING:
			{
				havingClause();
				break;
			}
			case EOF:
			case ORDER:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			switch ( LA(1)) {
			case ORDER:
			{
				orderByClause();
				break;
			}
			case EOF:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			match(Token.EOF_TYPE);
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_0);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void selectClause() throws RecognitionException, TokenStreamException {
		
		Token  t = null;
		
		try {      // for error handling
			t = LT(1);
			match(SELECT);
			{
			switch ( LA(1)) {
			case DISTINCT:
			{
				match(DISTINCT);
				break;
			}
			case AVG:
			case COUNT:
			case MAX:
			case MIN:
			case NEW:
			case OBJECT:
			case SUM:
			case IDENT:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			selectExpression();
			{
			_loop10:
			do {
				if ((LA(1)==COMMA)) {
					match(COMMA);
					selectExpression();
				}
				else {
					break _loop10;
				}
				
			} while (true);
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_1);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void fromClause() throws RecognitionException, TokenStreamException {
		
		Token  t = null;
		
		try {      // for error handling
			t = LT(1);
			match(FROM);
			identificationVariableDeclaration();
			{
			_loop29:
			do {
				if ((LA(1)==COMMA)) {
					match(COMMA);
					{
					if (((LA(1) >= ABS && LA(1) <= FLOAT_SUFFIX)) && (LA(2)==AS||LA(2)==IDENT)) {
						identificationVariableDeclaration();
					}
					else if ((LA(1)==IN) && (LA(2)==LEFT_ROUND_BRACKET)) {
						collectionMemberDeclaration();
					}
					else {
						throw new NoViableAltException(LT(1), getFilename());
					}
					
					}
				}
				else {
					break _loop29;
				}
				
			} while (true);
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_2);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void whereClause() throws RecognitionException, TokenStreamException {
		
		Token  t = null;
		
		try {      // for error handling
			t = LT(1);
			match(WHERE);
			conditionalExpression();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_3);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void groupByClause() throws RecognitionException, TokenStreamException {
		
		Token  g = null;
		
		try {      // for error handling
			g = LT(1);
			match(GROUP);
			match(BY);
			groupByItem();
			{
			_loop159:
			do {
				if ((LA(1)==COMMA)) {
					match(COMMA);
					groupByItem();
				}
				else {
					break _loop159;
				}
				
			} while (true);
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_4);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void havingClause() throws RecognitionException, TokenStreamException {
		
		Token  h = null;
		
		try {      // for error handling
			h = LT(1);
			match(HAVING);
			if ( inputState.guessing==0 ) {
				setAggregatesAllowed(true);
			}
			conditionalExpression();
			if ( inputState.guessing==0 ) {
				
				setAggregatesAllowed(false); 
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_5);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void orderByClause() throws RecognitionException, TokenStreamException {
		
		Token  o = null;
		
		try {      // for error handling
			o = LT(1);
			match(ORDER);
			match(BY);
			orderByItem();
			{
			_loop154:
			do {
				if ((LA(1)==COMMA)) {
					match(COMMA);
					orderByItem();
				}
				else {
					break _loop154;
				}
				
			} while (true);
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_0);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void selectExpression() throws RecognitionException, TokenStreamException {
		
		Class variableClass;
		
		try {      // for error handling
			switch ( LA(1)) {
			case AVG:
			case COUNT:
			case MAX:
			case MIN:
			case SUM:
			{
				variableClass=aggregateExpression();
				break;
			}
			case OBJECT:
			{
				match(OBJECT);
				match(LEFT_ROUND_BRACKET);
				variableClass=variableAccess();
				match(RIGHT_ROUND_BRACKET);
				break;
			}
			case NEW:
			{
				constructorExpression();
				break;
			}
			default:
				if ((LA(1)==IDENT) && (LA(2)==DOT)) {
					variableClass=singleValuedPathExpression();
				}
				else if ((LA(1)==IDENT) && (LA(2)==FROM||LA(2)==COMMA)) {
					variableClass=variableAccess();
				}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_6);
			} else {
			  throw ex;
			}
		}
	}
	
	public final Class  singleValuedPathExpression() throws RecognitionException, TokenStreamException {
		Class retval;
		
		retval = null;
		
		try {      // for error handling
			retval=pathExpression();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_7);
			} else {
			  throw ex;
			}
		}
		return retval;
	}
	
	public final Class  aggregateExpression() throws RecognitionException, TokenStreamException {
		Class retval;
		
		Token  t1 = null;
		Token  t2 = null;
		Token  t3 = null;
		Token  t4 = null;
		Token  t5 = null;
		
		Class variableClass = null; 
		retval = null;
		
		
		try {      // for error handling
			switch ( LA(1)) {
			case AVG:
			{
				t1 = LT(1);
				match(AVG);
				match(LEFT_ROUND_BRACKET);
				{
				switch ( LA(1)) {
				case DISTINCT:
				{
					match(DISTINCT);
					break;
				}
				case IDENT:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				variableClass=stateFieldPathExpression();
				match(RIGHT_ROUND_BRACKET);
				if ( inputState.guessing==0 ) {
					retval = variableClass;
				}
				break;
			}
			case MAX:
			{
				t2 = LT(1);
				match(MAX);
				match(LEFT_ROUND_BRACKET);
				{
				switch ( LA(1)) {
				case DISTINCT:
				{
					match(DISTINCT);
					break;
				}
				case IDENT:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				variableClass=stateFieldPathExpression();
				match(RIGHT_ROUND_BRACKET);
				if ( inputState.guessing==0 ) {
					retval = variableClass;
				}
				break;
			}
			case MIN:
			{
				t3 = LT(1);
				match(MIN);
				match(LEFT_ROUND_BRACKET);
				{
				switch ( LA(1)) {
				case DISTINCT:
				{
					match(DISTINCT);
					break;
				}
				case IDENT:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				variableClass=stateFieldPathExpression();
				match(RIGHT_ROUND_BRACKET);
				if ( inputState.guessing==0 ) {
					retval = variableClass;
				}
				break;
			}
			case SUM:
			{
				t4 = LT(1);
				match(SUM);
				match(LEFT_ROUND_BRACKET);
				{
				switch ( LA(1)) {
				case DISTINCT:
				{
					match(DISTINCT);
					break;
				}
				case IDENT:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				variableClass=stateFieldPathExpression();
				match(RIGHT_ROUND_BRACKET);
				if ( inputState.guessing==0 ) {
					retval = variableClass;
				}
				break;
			}
			case COUNT:
			{
				t5 = LT(1);
				match(COUNT);
				match(LEFT_ROUND_BRACKET);
				{
				switch ( LA(1)) {
				case DISTINCT:
				{
					match(DISTINCT);
					break;
				}
				case IDENT:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				{
				if ((LA(1)==IDENT) && (LA(2)==RIGHT_ROUND_BRACKET)) {
					variableClass=variableAccess();
				}
				else if ((LA(1)==IDENT) && (LA(2)==DOT)) {
					variableClass=pathExpression();
				}
				else {
					throw new NoViableAltException(LT(1), getFilename());
				}
				
				}
				match(RIGHT_ROUND_BRACKET);
				if ( inputState.guessing==0 ) {
					retval = Integer.TYPE;
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_8);
			} else {
			  throw ex;
			}
		}
		return retval;
	}
	
	public final Class  variableAccess() throws RecognitionException, TokenStreamException {
		Class variableClass;
		
		Token  i = null;
		
		variableClass = null; 
		String variableName = null;
		
		
		try {      // for error handling
			i = LT(1);
			match(IDENT);
			if ( inputState.guessing==0 ) {
				
					  variableName = i.getText();
					  variableClass = (Class)entityClassByAliasName.get(variableName);
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_9);
			} else {
			  throw ex;
			}
		}
		return variableClass;
	}
	
	public final void constructorExpression() throws RecognitionException, TokenStreamException {
		
		Token  t = null;
		
		try {      // for error handling
			t = LT(1);
			match(NEW);
			constructorName();
			match(LEFT_ROUND_BRACKET);
			constructorItem();
			{
			_loop21:
			do {
				if ((LA(1)==COMMA)) {
					match(COMMA);
					constructorItem();
				}
				else {
					break _loop21;
				}
				
			} while (true);
			}
			match(RIGHT_ROUND_BRACKET);
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_6);
			} else {
			  throw ex;
			}
		}
	}
	
	public final Class  stateFieldPathExpression() throws RecognitionException, TokenStreamException {
		Class retval;
		
		retval = null;
		
		try {      // for error handling
			retval=pathExpression();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_10);
			} else {
			  throw ex;
			}
		}
		return retval;
	}
	
	public final Class  pathExpression() throws RecognitionException, TokenStreamException {
		Class variableClass;
		
		Token  d = null;
		
		String attributeName = null;
		variableClass = null; 
		
		
		try {      // for error handling
			variableClass=variableAccess();
			{
			int _cnt51=0;
			_loop51:
			do {
				if ((LA(1)==DOT)) {
					d = LT(1);
					match(DOT);
					attributeName=attribute();
					if ( inputState.guessing==0 ) {
						variableClass = getMemberType(variableClass, attributeName);
					}
				}
				else {
					if ( _cnt51>=1 ) { break _loop51; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt51++;
			} while (true);
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_11);
			} else {
			  throw ex;
			}
		}
		return variableClass;
	}
	
	public final void constructorName() throws RecognitionException, TokenStreamException {
		
		Token  i1 = null;
		Token  i2 = null;
		
		try {      // for error handling
			i1 = LT(1);
			match(IDENT);
			{
			_loop24:
			do {
				if ((LA(1)==DOT)) {
					match(DOT);
					i2 = LT(1);
					match(IDENT);
				}
				else {
					break _loop24;
				}
				
			} while (true);
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_12);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void constructorItem() throws RecognitionException, TokenStreamException {
		
		Class variableClass = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case IDENT:
			{
				variableClass=singleValuedPathExpression();
				break;
			}
			case AVG:
			case COUNT:
			case MAX:
			case MIN:
			case SUM:
			{
				variableClass=aggregateExpression();
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_13);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void identificationVariableDeclaration() throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			rangeVariableDeclaration();
			{
			_loop32:
			do {
				if ((LA(1)==INNER||LA(1)==JOIN||LA(1)==LEFT)) {
					join();
				}
				else {
					break _loop32;
				}
				
			} while (true);
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_14);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void collectionMemberDeclaration() throws RecognitionException, TokenStreamException {
		
		Token  t = null;
		Token  i = null;
		Class collectionClass = null;
		
		try {      // for error handling
			t = LT(1);
			match(IN);
			match(LEFT_ROUND_BRACKET);
			collectionClass=collectionValuedPathExpression();
			match(RIGHT_ROUND_BRACKET);
			{
			switch ( LA(1)) {
			case AS:
			{
				match(AS);
				break;
			}
			case IDENT:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			i = LT(1);
			match(IDENT);
			if ( inputState.guessing==0 ) {
				
					if(collectionClass != null) { 
				String alias = i.getText();
				if(alias == null) alias = collectionClass.getName();
				entityClassByAliasName.put(alias, collectionClass);
				}
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_14);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void rangeVariableDeclaration() throws RecognitionException, TokenStreamException {
		
		Token  i = null;
		
		String schema;
		
		
		try {      // for error handling
			schema=abstractSchemaName();
			{
			switch ( LA(1)) {
			case AS:
			{
				match(AS);
				break;
			}
			case IDENT:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			i = LT(1);
			match(IDENT);
			if ( inputState.guessing==0 ) {
				
					    String alias = i.getText();
				if(alias == null) alias = schema;
					    entityClassByAliasName.put(alias, getClassFromEntityName(schema));
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_15);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void join() throws RecognitionException, TokenStreamException {
		
		Token  i = null;
		Token  t = null;
		
		Class joinClass = null;
		
		
		try {      // for error handling
			joinSpec();
			{
			switch ( LA(1)) {
			case IDENT:
			{
				joinClass=joinAssociationPathExpression();
				{
				switch ( LA(1)) {
				case AS:
				{
					match(AS);
					break;
				}
				case IDENT:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				i = LT(1);
				match(IDENT);
				if ( inputState.guessing==0 ) {
					
					String alias = i.getText();
					if(alias == null) alias = joinClass.getName();
						    entityClassByAliasName.put(alias, joinClass);
					
				}
				break;
			}
			case FETCH:
			{
				t = LT(1);
				match(FETCH);
				joinClass=joinAssociationPathExpression();
				if ( inputState.guessing==0 ) {
					
					String alias = t.getText();
					if(alias == null) alias = joinClass.getName();
						    entityClassByAliasName.put(alias, joinClass);
					
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_15);
			} else {
			  throw ex;
			}
		}
	}
	
	public final String  abstractSchemaName() throws RecognitionException, TokenStreamException {
		String schema;
		
		Token  ident = null;
		schema = null;
		
		try {      // for error handling
			ident = LT(1);
			matchNot(EOF);
			if ( inputState.guessing==0 ) {
				
				schema = ident.getText();
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_16);
			} else {
			  throw ex;
			}
		}
		return schema;
	}
	
	public final void joinSpec() throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case LEFT:
			{
				match(LEFT);
				{
				switch ( LA(1)) {
				case OUTER:
				{
					match(OUTER);
					break;
				}
				case JOIN:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				break;
			}
			case INNER:
			{
				match(INNER);
				break;
			}
			case JOIN:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			match(JOIN);
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_17);
			} else {
			  throw ex;
			}
		}
	}
	
	public final Class  joinAssociationPathExpression() throws RecognitionException, TokenStreamException {
		Class retval;
		
		Token  d = null;
		
		Class variableClass = null; 
		String attributeName = null;
		retval = null;
		
		
		try {      // for error handling
			variableClass=variableAccess();
			d = LT(1);
			match(DOT);
			attributeName=attribute();
			if ( inputState.guessing==0 ) {
				retval = getMemberType(variableClass, attributeName);
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_18);
			} else {
			  throw ex;
			}
		}
		return retval;
	}
	
	public final Class  collectionValuedPathExpression() throws RecognitionException, TokenStreamException {
		Class retval;
		
		retval = null;
		
		try {      // for error handling
			retval=pathExpression();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_19);
			} else {
			  throw ex;
			}
		}
		return retval;
	}
	
	public final Class  associationPathExpression() throws RecognitionException, TokenStreamException {
		Class retval;
		
		retval = null;
		
		try {      // for error handling
			retval=pathExpression();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_16);
			} else {
			  throw ex;
			}
		}
		return retval;
	}
	
	public final String  attribute() throws RecognitionException, TokenStreamException {
		String attributeName;
		
		Token  i = null;
		attributeName = null;
		
		try {      // for error handling
			i = LT(1);
			matchNot(EOF);
			if ( inputState.guessing==0 ) {
				
					    attributeName = i.getText();
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_20);
			} else {
			  throw ex;
			}
		}
		return attributeName;
	}
	
	public final void conditionalExpression() throws RecognitionException, TokenStreamException {
		
		Token  t = null;
		
		try {      // for error handling
			conditionalTerm();
			{
			_loop57:
			do {
				if ((LA(1)==OR)) {
					t = LT(1);
					match(OR);
					conditionalTerm();
				}
				else {
					break _loop57;
				}
				
			} while (true);
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_3);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void conditionalTerm() throws RecognitionException, TokenStreamException {
		
		Token  t = null;
		
		try {      // for error handling
			conditionalFactor();
			{
			_loop60:
			do {
				if ((LA(1)==AND)) {
					t = LT(1);
					match(AND);
					conditionalFactor();
				}
				else {
					break _loop60;
				}
				
			} while (true);
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_21);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void conditionalFactor() throws RecognitionException, TokenStreamException {
		
		Token  n = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case NOT:
			{
				n = LT(1);
				match(NOT);
				break;
			}
			case ABS:
			case AVG:
			case CONCAT:
			case COUNT:
			case CURRENT_DATE:
			case CURRENT_TIME:
			case CURRENT_TIMESTAMP:
			case EXISTS:
			case FALSE:
			case LENGTH:
			case LOCATE:
			case LOWER:
			case MAX:
			case MIN:
			case MOD:
			case SIZE:
			case SQRT:
			case SUBSTRING:
			case SUM:
			case TRIM:
			case TRUE:
			case UPPER:
			case LEFT_ROUND_BRACKET:
			case IDENT:
			case PLUS:
			case MINUS:
			case NUM_INT:
			case NUM_LONG:
			case NUM_FLOAT:
			case NUM_DOUBLE:
			case STRING_LITERAL_DOUBLE_QUOTED:
			case STRING_LITERAL_SINGLE_QUOTED:
			case POSITIONAL_PARAM:
			case NAMED_PARAM:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			switch ( LA(1)) {
			case ABS:
			case AVG:
			case CONCAT:
			case COUNT:
			case CURRENT_DATE:
			case CURRENT_TIME:
			case CURRENT_TIMESTAMP:
			case FALSE:
			case LENGTH:
			case LOCATE:
			case LOWER:
			case MAX:
			case MIN:
			case MOD:
			case SIZE:
			case SQRT:
			case SUBSTRING:
			case SUM:
			case TRIM:
			case TRUE:
			case UPPER:
			case LEFT_ROUND_BRACKET:
			case IDENT:
			case PLUS:
			case MINUS:
			case NUM_INT:
			case NUM_LONG:
			case NUM_FLOAT:
			case NUM_DOUBLE:
			case STRING_LITERAL_DOUBLE_QUOTED:
			case STRING_LITERAL_SINGLE_QUOTED:
			case POSITIONAL_PARAM:
			case NAMED_PARAM:
			{
				conditionalPrimary();
				break;
			}
			case EXISTS:
			{
				existsExpression((n!=null));
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_19);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void conditionalPrimary() throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			boolean synPredMatched66 = false;
			if (((LA(1)==LEFT_ROUND_BRACKET) && (_tokenSet_22.member(LA(2))) && (_tokenSet_23.member(LA(3))))) {
				int _m66 = mark();
				synPredMatched66 = true;
				inputState.guessing++;
				try {
					{
					match(LEFT_ROUND_BRACKET);
					conditionalExpression();
					}
				}
				catch (RecognitionException pe) {
					synPredMatched66 = false;
				}
				rewind(_m66);
inputState.guessing--;
			}
			if ( synPredMatched66 ) {
				match(LEFT_ROUND_BRACKET);
				conditionalExpression();
				match(RIGHT_ROUND_BRACKET);
			}
			else if ((_tokenSet_24.member(LA(1))) && (_tokenSet_25.member(LA(2))) && ((LA(3) >= ABS && LA(3) <= FLOAT_SUFFIX))) {
				simpleConditionalExpression();
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_19);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void existsExpression(
		boolean not
	) throws RecognitionException, TokenStreamException {
		
		Token  t = null;
		
		try {      // for error handling
			t = LT(1);
			match(EXISTS);
			match(LEFT_ROUND_BRACKET);
			subquery();
			match(RIGHT_ROUND_BRACKET);
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_19);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void simpleConditionalExpression() throws RecognitionException, TokenStreamException {
		
		
			Class leftClass = null;
		
		
		try {      // for error handling
			leftClass=arithmeticExpression(null);
			simpleConditionalExpressionRemainder(leftClass);
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_19);
			} else {
			  throw ex;
			}
		}
	}
	
	public final Class  arithmeticExpression(
		Class leftClass
	) throws RecognitionException, TokenStreamException {
		Class retval;
		
		
		Class exprClass = null;
		retval = null;
		
		
		try {      // for error handling
			if ((_tokenSet_24.member(LA(1))) && (_tokenSet_26.member(LA(2)))) {
				exprClass=simpleArithmeticExpression(leftClass);
				if ( inputState.guessing==0 ) {
					retval = exprClass;
				}
			}
			else if ((LA(1)==LEFT_ROUND_BRACKET) && (LA(2)==SELECT)) {
				match(LEFT_ROUND_BRACKET);
				subquery();
				match(RIGHT_ROUND_BRACKET);
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_27);
			} else {
			  throw ex;
			}
		}
		return retval;
	}
	
	public final void simpleConditionalExpressionRemainder(
		Class leftClass
	) throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			if (((LA(1) >= EQUALS && LA(1) <= LESS_THAN_EQUAL_TO))) {
				comparisonExpression(leftClass);
			}
			else {
				boolean synPredMatched71 = false;
				if (((LA(1)==BETWEEN||LA(1)==NOT) && (_tokenSet_28.member(LA(2))) && (_tokenSet_29.member(LA(3))))) {
					int _m71 = mark();
					synPredMatched71 = true;
					inputState.guessing++;
					try {
						{
						{
						switch ( LA(1)) {
						case NOT:
						{
							match(NOT);
							break;
						}
						case BETWEEN:
						{
							break;
						}
						default:
						{
							throw new NoViableAltException(LT(1), getFilename());
						}
						}
						}
						match(BETWEEN);
						}
					}
					catch (RecognitionException pe) {
						synPredMatched71 = false;
					}
					rewind(_m71);
inputState.guessing--;
				}
				if ( synPredMatched71 ) {
					betweenExpression(leftClass);
				}
				else {
					boolean synPredMatched74 = false;
					if (((LA(1)==LIKE||LA(1)==NOT) && (_tokenSet_30.member(LA(2))) && (_tokenSet_31.member(LA(3))))) {
						int _m74 = mark();
						synPredMatched74 = true;
						inputState.guessing++;
						try {
							{
							{
							switch ( LA(1)) {
							case NOT:
							{
								match(NOT);
								break;
							}
							case LIKE:
							{
								break;
							}
							default:
							{
								throw new NoViableAltException(LT(1), getFilename());
							}
							}
							}
							match(LIKE);
							}
						}
						catch (RecognitionException pe) {
							synPredMatched74 = false;
						}
						rewind(_m74);
inputState.guessing--;
					}
					if ( synPredMatched74 ) {
						likeExpression(leftClass);
					}
					else {
						boolean synPredMatched77 = false;
						if (((LA(1)==IN||LA(1)==NOT) && (LA(2)==IN||LA(2)==LEFT_ROUND_BRACKET) && (_tokenSet_32.member(LA(3))))) {
							int _m77 = mark();
							synPredMatched77 = true;
							inputState.guessing++;
							try {
								{
								{
								switch ( LA(1)) {
								case NOT:
								{
									match(NOT);
									break;
								}
								case IN:
								{
									break;
								}
								default:
								{
									throw new NoViableAltException(LT(1), getFilename());
								}
								}
								}
								match(IN);
								}
							}
							catch (RecognitionException pe) {
								synPredMatched77 = false;
							}
							rewind(_m77);
inputState.guessing--;
						}
						if ( synPredMatched77 ) {
							inExpression(leftClass);
						}
						else {
							boolean synPredMatched80 = false;
							if (((LA(1)==IS) && (LA(2)==NOT||LA(2)==NULL) && (_tokenSet_33.member(LA(3))))) {
								int _m80 = mark();
								synPredMatched80 = true;
								inputState.guessing++;
								try {
									{
									match(IS);
									{
									switch ( LA(1)) {
									case NOT:
									{
										match(NOT);
										break;
									}
									case NULL:
									{
										break;
									}
									default:
									{
										throw new NoViableAltException(LT(1), getFilename());
									}
									}
									}
									match(NULL);
									}
								}
								catch (RecognitionException pe) {
									synPredMatched80 = false;
								}
								rewind(_m80);
inputState.guessing--;
							}
							if ( synPredMatched80 ) {
								nullComparisonExpression();
							}
							else if ((LA(1)==IS) && (LA(2)==EMPTY||LA(2)==NOT) && (_tokenSet_34.member(LA(3)))) {
								emptyCollectionComparisonExpression();
							}
							else if ((LA(1)==MEMBER||LA(1)==NOT) && (LA(2)==MEMBER||LA(2)==OF||LA(2)==IDENT) && (LA(3)==OF||LA(3)==IDENT||LA(3)==DOT)) {
								collectionMemberExpression(leftClass);
							}
							else {
								throw new NoViableAltException(LT(1), getFilename());
							}
							}}}}
						}
						catch (RecognitionException ex) {
							if (inputState.guessing==0) {
								reportError(ex);
								recover(ex,_tokenSet_19);
							} else {
							  throw ex;
							}
						}
					}
					
	public final void comparisonExpression(
		Class leftClass
	) throws RecognitionException, TokenStreamException {
		
		Token  t1 = null;
		Token  t2 = null;
		Token  t3 = null;
		Token  t4 = null;
		Token  t5 = null;
		Token  t6 = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case EQUALS:
			{
				t1 = LT(1);
				match(EQUALS);
				comparisonExpressionRightOperand(leftClass);
				break;
			}
			case NOT_EQUAL_TO:
			{
				t2 = LT(1);
				match(NOT_EQUAL_TO);
				comparisonExpressionRightOperand(leftClass);
				break;
			}
			case GREATER_THAN:
			{
				t3 = LT(1);
				match(GREATER_THAN);
				comparisonExpressionRightOperand(leftClass);
				break;
			}
			case GREATER_THAN_EQUAL_TO:
			{
				t4 = LT(1);
				match(GREATER_THAN_EQUAL_TO);
				comparisonExpressionRightOperand(leftClass);
				break;
			}
			case LESS_THAN:
			{
				t5 = LT(1);
				match(LESS_THAN);
				comparisonExpressionRightOperand(leftClass);
				break;
			}
			case LESS_THAN_EQUAL_TO:
			{
				t6 = LT(1);
				match(LESS_THAN_EQUAL_TO);
				comparisonExpressionRightOperand(leftClass);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_19);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void betweenExpression(
		Class leftClass
	) throws RecognitionException, TokenStreamException {
		
		Token  n = null;
		Token  t = null;
		
		Class exprClass = null;
		
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case NOT:
			{
				n = LT(1);
				match(NOT);
				break;
			}
			case BETWEEN:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			t = LT(1);
			match(BETWEEN);
			exprClass=arithmeticExpression(leftClass);
			match(AND);
			exprClass=arithmeticExpression(leftClass);
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_19);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void likeExpression(
		Class leftClass
	) throws RecognitionException, TokenStreamException {
		
		Token  n = null;
		Token  t = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case NOT:
			{
				n = LT(1);
				match(NOT);
				break;
			}
			case LIKE:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			t = LT(1);
			match(LIKE);
			likeValue(leftClass);
			{
			switch ( LA(1)) {
			case ESCAPE:
			{
				escape(leftClass);
				break;
			}
			case EOF:
			case AND:
			case GROUP:
			case HAVING:
			case OR:
			case ORDER:
			case RIGHT_ROUND_BRACKET:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_19);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void inExpression(
		Class leftClass
	) throws RecognitionException, TokenStreamException {
		
		Token  n = null;
		Token  t = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case NOT:
			{
				n = LT(1);
				match(NOT);
				break;
			}
			case IN:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			t = LT(1);
			match(IN);
			match(LEFT_ROUND_BRACKET);
			{
			switch ( LA(1)) {
			case NUM_INT:
			case NUM_LONG:
			case NUM_FLOAT:
			case NUM_DOUBLE:
			case STRING_LITERAL_DOUBLE_QUOTED:
			case STRING_LITERAL_SINGLE_QUOTED:
			case POSITIONAL_PARAM:
			case NAMED_PARAM:
			{
				inItem(leftClass);
				{
				_loop87:
				do {
					if ((LA(1)==COMMA)) {
						match(COMMA);
						inItem(leftClass);
					}
					else {
						break _loop87;
					}
					
				} while (true);
				}
				break;
			}
			case SELECT:
			{
				subquery();
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			match(RIGHT_ROUND_BRACKET);
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_19);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void nullComparisonExpression() throws RecognitionException, TokenStreamException {
		
		Token  t = null;
		Token  n = null;
		
		try {      // for error handling
			t = LT(1);
			match(IS);
			{
			switch ( LA(1)) {
			case NOT:
			{
				n = LT(1);
				match(NOT);
				break;
			}
			case NULL:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			match(NULL);
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_19);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void emptyCollectionComparisonExpression() throws RecognitionException, TokenStreamException {
		
		Token  t = null;
		Token  n = null;
		
		try {      // for error handling
			t = LT(1);
			match(IS);
			{
			switch ( LA(1)) {
			case NOT:
			{
				n = LT(1);
				match(NOT);
				break;
			}
			case EMPTY:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			match(EMPTY);
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_19);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void collectionMemberExpression(
		Class leftClass
	) throws RecognitionException, TokenStreamException {
		
		Token  n = null;
		Token  t = null;
		Class exprClass = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case NOT:
			{
				n = LT(1);
				match(NOT);
				break;
			}
			case MEMBER:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			t = LT(1);
			match(MEMBER);
			{
			switch ( LA(1)) {
			case OF:
			{
				match(OF);
				break;
			}
			case IDENT:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			exprClass=collectionValuedPathExpression();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_19);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void inItem(
		Class leftClass
	) throws RecognitionException, TokenStreamException {
		
		Class itemClass = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case STRING_LITERAL_DOUBLE_QUOTED:
			case STRING_LITERAL_SINGLE_QUOTED:
			{
				literalString();
				break;
			}
			case NUM_INT:
			case NUM_LONG:
			case NUM_FLOAT:
			case NUM_DOUBLE:
			{
				itemClass=literalNumeric();
				break;
			}
			case POSITIONAL_PARAM:
			case NAMED_PARAM:
			{
				inputParameter(leftClass);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_13);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void subquery() throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			simpleSelectClause();
			subqueryFromClause();
			{
			switch ( LA(1)) {
			case WHERE:
			{
				whereClause();
				break;
			}
			case GROUP:
			case HAVING:
			case RIGHT_ROUND_BRACKET:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			switch ( LA(1)) {
			case GROUP:
			{
				groupByClause();
				break;
			}
			case HAVING:
			case RIGHT_ROUND_BRACKET:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			switch ( LA(1)) {
			case HAVING:
			{
				havingClause();
				break;
			}
			case RIGHT_ROUND_BRACKET:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_35);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void literalString() throws RecognitionException, TokenStreamException {
		
		Token  d = null;
		Token  s = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case STRING_LITERAL_DOUBLE_QUOTED:
			{
				d = LT(1);
				match(STRING_LITERAL_DOUBLE_QUOTED);
				break;
			}
			case STRING_LITERAL_SINGLE_QUOTED:
			{
				s = LT(1);
				match(STRING_LITERAL_SINGLE_QUOTED);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_36);
			} else {
			  throw ex;
			}
		}
	}
	
	public final Class  literalNumeric() throws RecognitionException, TokenStreamException {
		Class retval;
		
		Token  i = null;
		Token  l = null;
		Token  f = null;
		Token  d = null;
		retval = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case NUM_INT:
			{
				i = LT(1);
				match(NUM_INT);
				if ( inputState.guessing==0 ) {
					retval = Integer.TYPE;
				}
				break;
			}
			case NUM_LONG:
			{
				l = LT(1);
				match(NUM_LONG);
				if ( inputState.guessing==0 ) {
					retval = Long.TYPE;
				}
				break;
			}
			case NUM_FLOAT:
			{
				f = LT(1);
				match(NUM_FLOAT);
				if ( inputState.guessing==0 ) {
					retval = Float.TYPE;
				}
				break;
			}
			case NUM_DOUBLE:
			{
				d = LT(1);
				match(NUM_DOUBLE);
				if ( inputState.guessing==0 ) {
					retval = Double.TYPE;
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_37);
			} else {
			  throw ex;
			}
		}
		return retval;
	}
	
	public final void inputParameter(
		Class leftClass
	) throws RecognitionException, TokenStreamException {
		
		Token  p = null;
		Token  n = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case POSITIONAL_PARAM:
			{
				p = LT(1);
				match(POSITIONAL_PARAM);
				if ( inputState.guessing==0 ) {
					
					// skip the leading ?
					String text = p.getText().substring(1);
					paramTypesByName.put(text, leftClass);
					
				}
				break;
			}
			case NAMED_PARAM:
			{
				n = LT(1);
				match(NAMED_PARAM);
				if ( inputState.guessing==0 ) {
					
					// skip the leading :
					String text = n.getText().substring(1);
					paramTypesByName.put(text, leftClass);
					
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_36);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void likeValue(
		Class leftClass
	) throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			switch ( LA(1)) {
			case STRING_LITERAL_DOUBLE_QUOTED:
			case STRING_LITERAL_SINGLE_QUOTED:
			{
				literalString();
				break;
			}
			case POSITIONAL_PARAM:
			case NAMED_PARAM:
			{
				inputParameter(String.class);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_38);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void escape(
		Class leftClass
	) throws RecognitionException, TokenStreamException {
		
		Token  t = null;
		
		try {      // for error handling
			t = LT(1);
			match(ESCAPE);
			likeValue(leftClass);
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_19);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void comparisonExpressionRightOperand(
		Class leftClass
	) throws RecognitionException, TokenStreamException {
		
		
		Class exprClass = null;
		
		
		try {      // for error handling
			switch ( LA(1)) {
			case ABS:
			case AVG:
			case CONCAT:
			case COUNT:
			case CURRENT_DATE:
			case CURRENT_TIME:
			case CURRENT_TIMESTAMP:
			case FALSE:
			case LENGTH:
			case LOCATE:
			case LOWER:
			case MAX:
			case MIN:
			case MOD:
			case SIZE:
			case SQRT:
			case SUBSTRING:
			case SUM:
			case TRIM:
			case TRUE:
			case UPPER:
			case LEFT_ROUND_BRACKET:
			case IDENT:
			case PLUS:
			case MINUS:
			case NUM_INT:
			case NUM_LONG:
			case NUM_FLOAT:
			case NUM_DOUBLE:
			case STRING_LITERAL_DOUBLE_QUOTED:
			case STRING_LITERAL_SINGLE_QUOTED:
			case POSITIONAL_PARAM:
			case NAMED_PARAM:
			{
				exprClass=arithmeticExpression(leftClass);
				break;
			}
			case ALL:
			case ANY:
			case SOME:
			{
				anyOrAllExpression(leftClass);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_19);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void anyOrAllExpression(
		Class leftClass
	) throws RecognitionException, TokenStreamException {
		
		Token  a = null;
		Token  y = null;
		Token  s = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case ALL:
			{
				a = LT(1);
				match(ALL);
				match(LEFT_ROUND_BRACKET);
				subquery();
				match(RIGHT_ROUND_BRACKET);
				break;
			}
			case ANY:
			{
				y = LT(1);
				match(ANY);
				match(LEFT_ROUND_BRACKET);
				subquery();
				match(RIGHT_ROUND_BRACKET);
				break;
			}
			case SOME:
			{
				s = LT(1);
				match(SOME);
				match(LEFT_ROUND_BRACKET);
				subquery();
				match(RIGHT_ROUND_BRACKET);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_19);
			} else {
			  throw ex;
			}
		}
	}
	
	public final Class  simpleArithmeticExpression(
		Class leftClass
	) throws RecognitionException, TokenStreamException {
		Class retval;
		
		Token  p = null;
		Token  m = null;
		
		Class exprClass = null;
		retval = null;
		if(leftClass == null) leftClass = Integer.TYPE;
		
		
		try {      // for error handling
			exprClass=arithmeticTerm(leftClass);
			if ( inputState.guessing==0 ) {
				retval = exprClass;
			}
			{
			_loop107:
			do {
				switch ( LA(1)) {
				case PLUS:
				{
					p = LT(1);
					match(PLUS);
					exprClass=arithmeticTerm(exprClass);
					if ( inputState.guessing==0 ) {
						retval = exprClass;
					}
					break;
				}
				case MINUS:
				{
					m = LT(1);
					match(MINUS);
					exprClass=arithmeticTerm(exprClass);
					if ( inputState.guessing==0 ) {
						retval = exprClass;
					}
					break;
				}
				default:
				{
					break _loop107;
				}
				}
			} while (true);
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_39);
			} else {
			  throw ex;
			}
		}
		return retval;
	}
	
	public final Class  arithmeticTerm(
		Class leftClass
	) throws RecognitionException, TokenStreamException {
		Class retval;
		
		Token  m = null;
		Token  d = null;
		
		Class exprClass = null;
		retval = null;
		
		
		try {      // for error handling
			exprClass=arithmeticFactor(leftClass);
			if ( inputState.guessing==0 ) {
				retval = exprClass;
			}
			{
			_loop110:
			do {
				switch ( LA(1)) {
				case MULTIPLY:
				{
					m = LT(1);
					match(MULTIPLY);
					exprClass=arithmeticFactor(exprClass);
					if ( inputState.guessing==0 ) {
						retval = exprClass;
					}
					break;
				}
				case DIVIDE:
				{
					d = LT(1);
					match(DIVIDE);
					exprClass=arithmeticFactor(exprClass);
					if ( inputState.guessing==0 ) {
						retval = exprClass;
					}
					break;
				}
				default:
				{
					break _loop110;
				}
				}
			} while (true);
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_40);
			} else {
			  throw ex;
			}
		}
		return retval;
	}
	
	public final Class  arithmeticFactor(
		Class leftClass
	) throws RecognitionException, TokenStreamException {
		Class retval;
		
		Token  p = null;
		Token  m = null;
		
		Class exprClass = null;
		retval = null; 
		
		
		try {      // for error handling
			switch ( LA(1)) {
			case PLUS:
			{
				p = LT(1);
				match(PLUS);
				exprClass=arithmeticPrimary(leftClass);
				if ( inputState.guessing==0 ) {
					retval = exprClass;
				}
				break;
			}
			case MINUS:
			{
				m = LT(1);
				match(MINUS);
				exprClass=arithmeticPrimary(leftClass);
				if ( inputState.guessing==0 ) {
					retval = exprClass;
				}
				break;
			}
			case ABS:
			case AVG:
			case CONCAT:
			case COUNT:
			case CURRENT_DATE:
			case CURRENT_TIME:
			case CURRENT_TIMESTAMP:
			case FALSE:
			case LENGTH:
			case LOCATE:
			case LOWER:
			case MAX:
			case MIN:
			case MOD:
			case SIZE:
			case SQRT:
			case SUBSTRING:
			case SUM:
			case TRIM:
			case TRUE:
			case UPPER:
			case LEFT_ROUND_BRACKET:
			case IDENT:
			case NUM_INT:
			case NUM_LONG:
			case NUM_FLOAT:
			case NUM_DOUBLE:
			case STRING_LITERAL_DOUBLE_QUOTED:
			case STRING_LITERAL_SINGLE_QUOTED:
			case POSITIONAL_PARAM:
			case NAMED_PARAM:
			{
				exprClass=arithmeticPrimary(leftClass);
				if ( inputState.guessing==0 ) {
					retval = exprClass;
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_37);
			} else {
			  throw ex;
			}
		}
		return retval;
	}
	
	public final Class  arithmeticPrimary(
		Class leftClass
	) throws RecognitionException, TokenStreamException {
		Class retval;
		
		
		Class variableClass = null; 
		retval = null; 
		
		
		try {      // for error handling
			switch ( LA(1)) {
			case ABS:
			case LENGTH:
			case LOCATE:
			case MOD:
			case SIZE:
			case SQRT:
			{
				variableClass=functionsReturningNumerics();
				if ( inputState.guessing==0 ) {
					retval = variableClass;
				}
				break;
			}
			case CURRENT_DATE:
			case CURRENT_TIME:
			case CURRENT_TIMESTAMP:
			{
				variableClass=functionsReturningDatetime();
				if ( inputState.guessing==0 ) {
					retval = variableClass;
				}
				break;
			}
			case CONCAT:
			case LOWER:
			case SUBSTRING:
			case TRIM:
			case UPPER:
			{
				functionsReturningStrings();
				if ( inputState.guessing==0 ) {
					retval = String.class;
				}
				break;
			}
			case POSITIONAL_PARAM:
			case NAMED_PARAM:
			{
				inputParameter(leftClass);
				if ( inputState.guessing==0 ) {
					retval = leftClass;
				}
				break;
			}
			case NUM_INT:
			case NUM_LONG:
			case NUM_FLOAT:
			case NUM_DOUBLE:
			{
				variableClass=literalNumeric();
				if ( inputState.guessing==0 ) {
					retval = variableClass;
				}
				break;
			}
			case STRING_LITERAL_DOUBLE_QUOTED:
			case STRING_LITERAL_SINGLE_QUOTED:
			{
				literalString();
				if ( inputState.guessing==0 ) {
					retval = String.class;
				}
				break;
			}
			case FALSE:
			case TRUE:
			{
				literalBoolean();
				if ( inputState.guessing==0 ) {
					retval = Boolean.TYPE;
				}
				break;
			}
			case LEFT_ROUND_BRACKET:
			{
				match(LEFT_ROUND_BRACKET);
				variableClass=simpleArithmeticExpression(leftClass);
				match(RIGHT_ROUND_BRACKET);
				if ( inputState.guessing==0 ) {
					retval = variableClass;
				}
				break;
			}
			default:
				if (((_tokenSet_41.member(LA(1))))&&( aggregatesAllowed() )) {
					variableClass=aggregateExpression();
					if ( inputState.guessing==0 ) {
						retval = variableClass;
					}
				}
				else if ((LA(1)==IDENT) && (_tokenSet_37.member(LA(2)))) {
					variableClass=variableAccess();
					if ( inputState.guessing==0 ) {
						retval = variableClass;
					}
				}
				else if ((LA(1)==IDENT) && (LA(2)==DOT)) {
					variableClass=stateFieldPathExpression();
					if ( inputState.guessing==0 ) {
						retval = variableClass;
					}
				}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_37);
			} else {
			  throw ex;
			}
		}
		return retval;
	}
	
	public final Class  functionsReturningNumerics() throws RecognitionException, TokenStreamException {
		Class retval;
		
		retval = null; 
		Class functionClass = null;
		
		
		try {      // for error handling
			switch ( LA(1)) {
			case ABS:
			{
				functionClass=abs();
				if ( inputState.guessing==0 ) {
					retval = functionClass;
				}
				break;
			}
			case LENGTH:
			{
				length();
				if ( inputState.guessing==0 ) {
					retval = Integer.TYPE;
				}
				break;
			}
			case MOD:
			{
				mod();
				if ( inputState.guessing==0 ) {
					retval = Integer.TYPE;
				}
				break;
			}
			case SQRT:
			{
				functionClass=sqrt();
				if ( inputState.guessing==0 ) {
					retval = functionClass;
				}
				break;
			}
			case LOCATE:
			{
				locate();
				if ( inputState.guessing==0 ) {
					retval = Integer.TYPE;
				}
				break;
			}
			case SIZE:
			{
				size();
				if ( inputState.guessing==0 ) {
					retval = Integer.TYPE;
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_37);
			} else {
			  throw ex;
			}
		}
		return retval;
	}
	
	public final Class  functionsReturningDatetime() throws RecognitionException, TokenStreamException {
		Class retval;
		
		Token  d = null;
		Token  t = null;
		Token  ts = null;
		retval = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case CURRENT_DATE:
			{
				d = LT(1);
				match(CURRENT_DATE);
				if ( inputState.guessing==0 ) {
					retval = java.sql.Date.class;
				}
				break;
			}
			case CURRENT_TIME:
			{
				t = LT(1);
				match(CURRENT_TIME);
				if ( inputState.guessing==0 ) {
					retval = java.sql.Time.class;
				}
				break;
			}
			case CURRENT_TIMESTAMP:
			{
				ts = LT(1);
				match(CURRENT_TIMESTAMP);
				if ( inputState.guessing==0 ) {
					retval = java.sql.Timestamp.class;
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_37);
			} else {
			  throw ex;
			}
		}
		return retval;
	}
	
	public final void functionsReturningStrings() throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			switch ( LA(1)) {
			case CONCAT:
			{
				concat();
				break;
			}
			case SUBSTRING:
			{
				substring();
				break;
			}
			case TRIM:
			{
				trim();
				break;
			}
			case UPPER:
			{
				upper();
				break;
			}
			case LOWER:
			{
				lower();
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_37);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void literalBoolean() throws RecognitionException, TokenStreamException {
		
		Token  t = null;
		Token  f = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case TRUE:
			{
				t = LT(1);
				match(TRUE);
				break;
			}
			case FALSE:
			{
				f = LT(1);
				match(FALSE);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_37);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void stringPrimary() throws RecognitionException, TokenStreamException {
		
		Class variableClass = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case STRING_LITERAL_DOUBLE_QUOTED:
			case STRING_LITERAL_SINGLE_QUOTED:
			{
				literalString();
				break;
			}
			case CONCAT:
			case LOWER:
			case SUBSTRING:
			case TRIM:
			case UPPER:
			{
				functionsReturningStrings();
				break;
			}
			case POSITIONAL_PARAM:
			case NAMED_PARAM:
			{
				inputParameter(String.class);
				break;
			}
			case IDENT:
			{
				variableClass=stateFieldPathExpression();
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_13);
			} else {
			  throw ex;
			}
		}
	}
	
	public final Class  literal() throws RecognitionException, TokenStreamException {
		Class retval;
		
		
		Class literalClass = null;
		retval = null; 
		
		
		try {      // for error handling
			switch ( LA(1)) {
			case NUM_INT:
			case NUM_LONG:
			case NUM_FLOAT:
			case NUM_DOUBLE:
			{
				literalClass=literalNumeric();
				if ( inputState.guessing==0 ) {
					retval = literalClass;
				}
				break;
			}
			case FALSE:
			case TRUE:
			{
				literalBoolean();
				if ( inputState.guessing==0 ) {
					retval = Boolean.TYPE;
				}
				break;
			}
			case STRING_LITERAL_DOUBLE_QUOTED:
			case STRING_LITERAL_SINGLE_QUOTED:
			{
				literalString();
				if ( inputState.guessing==0 ) {
					retval = String.class;
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_0);
			} else {
			  throw ex;
			}
		}
		return retval;
	}
	
	public final Class  abs() throws RecognitionException, TokenStreamException {
		Class retval;
		
		Token  a = null;
		retval = null;
		
		try {      // for error handling
			a = LT(1);
			match(ABS);
			match(LEFT_ROUND_BRACKET);
			retval=simpleArithmeticExpression(null);
			match(RIGHT_ROUND_BRACKET);
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_37);
			} else {
			  throw ex;
			}
		}
		return retval;
	}
	
	public final void length() throws RecognitionException, TokenStreamException {
		
		Token  l = null;
		
		try {      // for error handling
			l = LT(1);
			match(LENGTH);
			match(LEFT_ROUND_BRACKET);
			stringPrimary();
			match(RIGHT_ROUND_BRACKET);
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_37);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void mod() throws RecognitionException, TokenStreamException {
		
		Token  m = null;
		Class exprClass = null;
		
		try {      // for error handling
			m = LT(1);
			match(MOD);
			match(LEFT_ROUND_BRACKET);
			exprClass=simpleArithmeticExpression(null);
			match(COMMA);
			exprClass=simpleArithmeticExpression(null);
			match(RIGHT_ROUND_BRACKET);
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_37);
			} else {
			  throw ex;
			}
		}
	}
	
	public final Class  sqrt() throws RecognitionException, TokenStreamException {
		Class retval;
		
		Token  s = null;
		retval = null;
		
		try {      // for error handling
			s = LT(1);
			match(SQRT);
			match(LEFT_ROUND_BRACKET);
			retval=simpleArithmeticExpression(null);
			match(RIGHT_ROUND_BRACKET);
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_37);
			} else {
			  throw ex;
			}
		}
		return retval;
	}
	
	public final void locate() throws RecognitionException, TokenStreamException {
		
		Token  l = null;
		Class exprClass = null;
		
		try {      // for error handling
			l = LT(1);
			match(LOCATE);
			match(LEFT_ROUND_BRACKET);
			stringPrimary();
			match(COMMA);
			stringPrimary();
			{
			switch ( LA(1)) {
			case COMMA:
			{
				match(COMMA);
				exprClass=simpleArithmeticExpression(Integer.TYPE);
				break;
			}
			case RIGHT_ROUND_BRACKET:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			match(RIGHT_ROUND_BRACKET);
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_37);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void size() throws RecognitionException, TokenStreamException {
		
		Token  s = null;
		Class exprClass = null;
		
		try {      // for error handling
			s = LT(1);
			match(SIZE);
			match(LEFT_ROUND_BRACKET);
			exprClass=collectionValuedPathExpression();
			match(RIGHT_ROUND_BRACKET);
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_37);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void concat() throws RecognitionException, TokenStreamException {
		
		Token  c = null;
		
		try {      // for error handling
			c = LT(1);
			match(CONCAT);
			match(LEFT_ROUND_BRACKET);
			stringPrimary();
			match(COMMA);
			stringPrimary();
			match(RIGHT_ROUND_BRACKET);
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_37);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void substring() throws RecognitionException, TokenStreamException {
		
		Token  s = null;
		Class exprClass = null;
		
		try {      // for error handling
			s = LT(1);
			match(SUBSTRING);
			match(LEFT_ROUND_BRACKET);
			stringPrimary();
			match(COMMA);
			exprClass=simpleArithmeticExpression(Integer.TYPE);
			match(COMMA);
			exprClass=simpleArithmeticExpression(Integer.TYPE);
			match(RIGHT_ROUND_BRACKET);
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_37);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void trim() throws RecognitionException, TokenStreamException {
		
		Token  t = null;
		
		try {      // for error handling
			t = LT(1);
			match(TRIM);
			match(LEFT_ROUND_BRACKET);
			{
			boolean synPredMatched128 = false;
			if (((_tokenSet_42.member(LA(1))) && (_tokenSet_43.member(LA(2))))) {
				int _m128 = mark();
				synPredMatched128 = true;
				inputState.guessing++;
				try {
					{
					trimSpec();
					trimChar();
					match(FROM);
					}
				}
				catch (RecognitionException pe) {
					synPredMatched128 = false;
				}
				rewind(_m128);
inputState.guessing--;
			}
			if ( synPredMatched128 ) {
				trimSpec();
				trimChar();
				match(FROM);
			}
			else if ((_tokenSet_44.member(LA(1))) && (LA(2)==LEFT_ROUND_BRACKET||LA(2)==RIGHT_ROUND_BRACKET||LA(2)==DOT)) {
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
			}
			stringPrimary();
			match(RIGHT_ROUND_BRACKET);
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_37);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void upper() throws RecognitionException, TokenStreamException {
		
		Token  u = null;
		
		try {      // for error handling
			u = LT(1);
			match(UPPER);
			match(LEFT_ROUND_BRACKET);
			stringPrimary();
			match(RIGHT_ROUND_BRACKET);
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_37);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void lower() throws RecognitionException, TokenStreamException {
		
		Token  l = null;
		
		try {      // for error handling
			l = LT(1);
			match(LOWER);
			match(LEFT_ROUND_BRACKET);
			stringPrimary();
			match(RIGHT_ROUND_BRACKET);
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_37);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void trimSpec() throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			switch ( LA(1)) {
			case LEADING:
			{
				match(LEADING);
				break;
			}
			case TRAILING:
			{
				match(TRAILING);
				break;
			}
			case BOTH:
			{
				match(BOTH);
				break;
			}
			case FROM:
			case STRING_LITERAL_DOUBLE_QUOTED:
			case STRING_LITERAL_SINGLE_QUOTED:
			case POSITIONAL_PARAM:
			case NAMED_PARAM:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_45);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void trimChar() throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			switch ( LA(1)) {
			case STRING_LITERAL_DOUBLE_QUOTED:
			case STRING_LITERAL_SINGLE_QUOTED:
			{
				literalString();
				break;
			}
			case POSITIONAL_PARAM:
			case NAMED_PARAM:
			{
				inputParameter(String.class);
				break;
			}
			case FROM:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_1);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void simpleSelectClause() throws RecognitionException, TokenStreamException {
		
		Token  s = null;
		
		try {      // for error handling
			s = LT(1);
			match(SELECT);
			{
			switch ( LA(1)) {
			case DISTINCT:
			{
				match(DISTINCT);
				break;
			}
			case AVG:
			case COUNT:
			case MAX:
			case MIN:
			case SUM:
			case IDENT:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			simpleSelectExpression();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_1);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void subqueryFromClause() throws RecognitionException, TokenStreamException {
		
		Token  f = null;
		
		try {      // for error handling
			f = LT(1);
			match(FROM);
			subselectIdentificationVariableDeclaration();
			{
			_loop149:
			do {
				if ((LA(1)==COMMA)) {
					match(COMMA);
					subselectIdentificationVariableDeclaration();
				}
				else {
					break _loop149;
				}
				
			} while (true);
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_46);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void simpleSelectExpression() throws RecognitionException, TokenStreamException {
		
		Class variableClass;
		
		try {      // for error handling
			if ((LA(1)==IDENT) && (LA(2)==DOT)) {
				variableClass=singleValuedPathExpression();
			}
			else if ((_tokenSet_41.member(LA(1)))) {
				variableClass=aggregateExpression();
			}
			else if ((LA(1)==IDENT) && (LA(2)==FROM)) {
				variableClass=variableAccess();
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_1);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void subselectIdentificationVariableDeclaration() throws RecognitionException, TokenStreamException {
		
		Token  i = null;
		
		Class variableClass = null;
		
		
		try {      // for error handling
			if (((LA(1) >= ABS && LA(1) <= FLOAT_SUFFIX)) && (LA(2)==AS||LA(2)==IDENT)) {
				identificationVariableDeclaration();
			}
			else if ((LA(1)==IDENT) && (LA(2)==DOT)) {
				variableClass=associationPathExpression();
				{
				switch ( LA(1)) {
				case AS:
				{
					match(AS);
					break;
				}
				case IDENT:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				i = LT(1);
				match(IDENT);
				if ( inputState.guessing==0 ) {
					
					String alias = i.getText();
					if(alias == null) alias = variableClass.getName();
					entityClassByAliasName.put(alias, variableClass);
					
				}
			}
			else if ((LA(1)==IN) && (LA(2)==LEFT_ROUND_BRACKET)) {
				collectionMemberDeclaration();
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_47);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void orderByItem() throws RecognitionException, TokenStreamException {
		
		Token  a = null;
		Token  d = null;
		Class variableClass = null;
		
		try {      // for error handling
			variableClass=stateFieldPathExpression();
			{
			switch ( LA(1)) {
			case ASC:
			{
				a = LT(1);
				match(ASC);
				break;
			}
			case DESC:
			{
				d = LT(1);
				match(DESC);
				break;
			}
			case EOF:
			case COMMA:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_48);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void groupByItem() throws RecognitionException, TokenStreamException {
		
		Class variableClass;
		
		try {      // for error handling
			if ((LA(1)==IDENT) && (LA(2)==DOT)) {
				variableClass=stateFieldPathExpression();
			}
			else if ((LA(1)==IDENT) && (_tokenSet_49.member(LA(2)))) {
				variableClass=variableAccess();
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_49);
			} else {
			  throw ex;
			}
		}
	}
	
	
	public static final String[] _tokenNames = {
		"<0>",
		"EOF",
		"<2>",
		"NULL_TREE_LOOKAHEAD",
		"\"abs\"",
		"\"all\"",
		"\"and\"",
		"\"any\"",
		"\"as\"",
		"\"asc\"",
		"\"avg\"",
		"\"between\"",
		"\"both\"",
		"\"by\"",
		"\"concat\"",
		"\"count\"",
		"\"current_date\"",
		"\"current_time\"",
		"\"current_timestamp\"",
		"\"desc\"",
		"\"delete\"",
		"\"distinct\"",
		"\"empty\"",
		"\"escape\"",
		"\"exists\"",
		"\"false\"",
		"\"fetch\"",
		"\"from\"",
		"\"group\"",
		"\"having\"",
		"\"in\"",
		"\"inner\"",
		"\"is\"",
		"\"join\"",
		"\"leading\"",
		"\"left\"",
		"\"length\"",
		"\"like\"",
		"\"locate\"",
		"\"lower\"",
		"\"max\"",
		"\"member\"",
		"\"min\"",
		"\"mod\"",
		"\"new\"",
		"\"not\"",
		"\"null\"",
		"\"object\"",
		"\"of\"",
		"\"or\"",
		"\"order\"",
		"\"outer\"",
		"\"select\"",
		"\"set\"",
		"\"size\"",
		"\"sqrt\"",
		"\"some\"",
		"\"substring\"",
		"\"sum\"",
		"\"trailing\"",
		"\"trim\"",
		"\"true\"",
		"\"unknown\"",
		"\"update\"",
		"\"upper\"",
		"\"where\"",
		"COMMA",
		"LEFT_ROUND_BRACKET",
		"RIGHT_ROUND_BRACKET",
		"IDENT",
		"DOT",
		"EQUALS",
		"NOT_EQUAL_TO",
		"GREATER_THAN",
		"GREATER_THAN_EQUAL_TO",
		"LESS_THAN",
		"LESS_THAN_EQUAL_TO",
		"PLUS",
		"MINUS",
		"MULTIPLY",
		"DIVIDE",
		"NUM_INT",
		"NUM_LONG",
		"NUM_FLOAT",
		"NUM_DOUBLE",
		"STRING_LITERAL_DOUBLE_QUOTED",
		"STRING_LITERAL_SINGLE_QUOTED",
		"POSITIONAL_PARAM",
		"NAMED_PARAM",
		"HEX_DIGIT",
		"WS",
		"TEXTCHAR",
		"EXPONENT",
		"FLOAT_SUFFIX"
	};
	
	private static final long[] mk_tokenSet_0() {
		long[] data = { 2L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
	private static final long[] mk_tokenSet_1() {
		long[] data = { 134217728L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
	private static final long[] mk_tokenSet_2() {
		long[] data = { 1125900712148994L, 2L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());
	private static final long[] mk_tokenSet_3() {
		long[] data = { 1125900712148994L, 16L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());
	private static final long[] mk_tokenSet_4() {
		long[] data = { 1125900443713538L, 16L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_4 = new BitSet(mk_tokenSet_4());
	private static final long[] mk_tokenSet_5() {
		long[] data = { 1125899906842626L, 16L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_5 = new BitSet(mk_tokenSet_5());
	private static final long[] mk_tokenSet_6() {
		long[] data = { 134217728L, 4L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_6 = new BitSet(mk_tokenSet_6());
	private static final long[] mk_tokenSet_7() {
		long[] data = { 134217728L, 20L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_7 = new BitSet(mk_tokenSet_7());
	private static final long[] mk_tokenSet_8() {
		long[] data = { 1726377002797122L, 130964L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_8 = new BitSet(mk_tokenSet_8());
	private static final long[] mk_tokenSet_9() {
		long[] data = { 1726377002797122L, 131028L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_9 = new BitSet(mk_tokenSet_9());
	private static final long[] mk_tokenSet_10() {
		long[] data = { 1726376869104194L, 130964L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_10 = new BitSet(mk_tokenSet_10());
	private static final long[] mk_tokenSet_11() {
		long[] data = { 1726377003322178L, 130996L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_11 = new BitSet(mk_tokenSet_11());
	private static final long[] mk_tokenSet_12() {
		long[] data = { 0L, 8L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_12 = new BitSet(mk_tokenSet_12());
	private static final long[] mk_tokenSet_13() {
		long[] data = { 0L, 20L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_13 = new BitSet(mk_tokenSet_13());
	private static final long[] mk_tokenSet_14() {
		long[] data = { 1125900712148994L, 22L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_14 = new BitSet(mk_tokenSet_14());
	private static final long[] mk_tokenSet_15() {
		long[] data = { 1125945809305602L, 22L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_15 = new BitSet(mk_tokenSet_15());
	private static final long[] mk_tokenSet_16() {
		long[] data = { 256L, 32L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_16 = new BitSet(mk_tokenSet_16());
	private static final long[] mk_tokenSet_17() {
		long[] data = { 67108864L, 32L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_17 = new BitSet(mk_tokenSet_17());
	private static final long[] mk_tokenSet_18() {
		long[] data = { 1125945809305858L, 54L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_18 = new BitSet(mk_tokenSet_18());
	private static final long[] mk_tokenSet_19() {
		long[] data = { 1688850665570370L, 16L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_19 = new BitSet(mk_tokenSet_19());
	private static final long[] mk_tokenSet_20() {
		long[] data = { 1726422100478786L, 131062L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_20 = new BitSet(mk_tokenSet_20());
	private static final long[] mk_tokenSet_21() {
		long[] data = { 1688850665570306L, 16L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_21 = new BitSet(mk_tokenSet_21());
	private static final long[] mk_tokenSet_22() {
		long[] data = { 3945203645003842576L, 33447977L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_22 = new BitSet(mk_tokenSet_22());
	private static final long[] mk_tokenSet_23() {
		long[] data = { 3949709586462133264L, 33554409L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_23 = new BitSet(mk_tokenSet_23());
	private static final long[] mk_tokenSet_24() {
		long[] data = { 3945168460614976528L, 33447977L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_24 = new BitSet(mk_tokenSet_24());
	private static final long[] mk_tokenSet_25() {
		long[] data = { 3949709586445356048L, 33554409L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_25 = new BitSet(mk_tokenSet_25());
	private static final long[] mk_tokenSet_26() {
		long[] data = { 3946894837483555922L, 33554425L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_26 = new BitSet(mk_tokenSet_26());
	private static final long[] mk_tokenSet_27() {
		long[] data = { 1726376868579394L, 8080L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_27 = new BitSet(mk_tokenSet_27());
	private static final long[] mk_tokenSet_28() {
		long[] data = { 3945168460614978576L, 33447977L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_28 = new BitSet(mk_tokenSet_28());
	private static final long[] mk_tokenSet_29() {
		long[] data = { 3949672060242347088L, 33546345L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_29 = new BitSet(mk_tokenSet_29());
	private static final long[] mk_tokenSet_30() {
		long[] data = { 137438953472L, 31457280L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_30 = new BitSet(mk_tokenSet_30());
	private static final long[] mk_tokenSet_31() {
		long[] data = { 1688850673958978L, 31457296L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_31 = new BitSet(mk_tokenSet_31());
	private static final long[] mk_tokenSet_32() {
		long[] data = { 4503599627370496L, 33423368L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_32 = new BitSet(mk_tokenSet_32());
	private static final long[] mk_tokenSet_33() {
		long[] data = { 1759219409748034L, 16L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_33 = new BitSet(mk_tokenSet_33());
	private static final long[] mk_tokenSet_34() {
		long[] data = { 1688850669764674L, 16L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_34 = new BitSet(mk_tokenSet_34());
	private static final long[] mk_tokenSet_35() {
		long[] data = { 0L, 16L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_35 = new BitSet(mk_tokenSet_35());
	private static final long[] mk_tokenSet_36() {
		long[] data = { 1726377011185730L, 130964L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_36 = new BitSet(mk_tokenSet_36());
	private static final long[] mk_tokenSet_37() {
		long[] data = { 1726376868579394L, 130964L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_37 = new BitSet(mk_tokenSet_37());
	private static final long[] mk_tokenSet_38() {
		long[] data = { 1688850673958978L, 16L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_38 = new BitSet(mk_tokenSet_38());
	private static final long[] mk_tokenSet_39() {
		long[] data = { 1726376868579394L, 8084L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_39 = new BitSet(mk_tokenSet_39());
	private static final long[] mk_tokenSet_40() {
		long[] data = { 1726376868579394L, 32660L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_40 = new BitSet(mk_tokenSet_40());
	private static final long[] mk_tokenSet_41() {
		long[] data = { 288235873709884416L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_41 = new BitSet(mk_tokenSet_41());
	private static final long[] mk_tokenSet_42() {
		long[] data = { 576460769617514496L, 31457280L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_42 = new BitSet(mk_tokenSet_42());
	private static final long[] mk_tokenSet_43() {
		long[] data = { 1297037242572750848L, 31457313L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_43 = new BitSet(mk_tokenSet_43());
	private static final long[] mk_tokenSet_44() {
		long[] data = { 1297037242438533120L, 31457313L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_44 = new BitSet(mk_tokenSet_44());
	private static final long[] mk_tokenSet_45() {
		long[] data = { 134217728L, 31457280L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_45 = new BitSet(mk_tokenSet_45());
	private static final long[] mk_tokenSet_46() {
		long[] data = { 805306368L, 18L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_46 = new BitSet(mk_tokenSet_46());
	private static final long[] mk_tokenSet_47() {
		long[] data = { 805306368L, 22L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_47 = new BitSet(mk_tokenSet_47());
	private static final long[] mk_tokenSet_48() {
		long[] data = { 2L, 4L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_48 = new BitSet(mk_tokenSet_48());
	private static final long[] mk_tokenSet_49() {
		long[] data = { 1125900443713538L, 20L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_49 = new BitSet(mk_tokenSet_49());
	
	}

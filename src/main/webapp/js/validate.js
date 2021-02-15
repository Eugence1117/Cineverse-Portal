/****************************************************************
* Seq		Date		Author		Description
* 1			09/01/2008	MTEA		add strKeyPress33, strKeyPress34, strKeyPressErr33, strKeyPressErr34
* 2			14/01/2008  LHYEE	    add ChkEmptyChecked(),getSelectedRadio(), getSelectedRadioValue(),ChkPassword()
* 3			11/02/2008  JOE			add strKeyPress35, strKeyPressErr35
* 4			12/02/2008	JOE			add strKeyPress36, strKeyPressErr36
* 5			19/02/2008  JOE			add validate_email
* 6			14/03/2008  JOE			strKeyPress36 change new Array( 42,42) to new Array( 44,44)
* 7			21/03/2008	MTEA		change strKeyPress10, add new Array(64,64); strKeyPress34, add new Array(45,45), new Array(33,33) ;
									strKeyPressErr10 ;
* 8			28/11/2008	LHYEE		add strKeyPress12 (add ' into strKeyPress10)
*************************************************************** */

var CONST_BYPASS_KEYCODE = ' keyValue == 0 || keyValue == 8 || keyValue == 13 || keyValue == 27 ';

var strKeyPress1	= new Array(new Array(65,90), new Array(97,122), new Array(48,57), new Array(46,46));
var strKeyPress2	= new Array(new Array(65,90), new Array(97,122), new Array(48,57), new Array(32,32));
var strKeyPress3	= new Array(new Array(48,57));
var strKeyPress4	= new Array(new Array(65,90), new Array(97,122), new Array(45,45));
var strKeyPress5	= new Array(new Array(65,90), new Array(97,122), new Array(32,32), new Array(40,40), new Array(47,47));
var strKeyPress6	= new Array(new Array(65,90), new Array(97,122), new Array(40,40), new Array(46,46), new Array(41,41), new Array(47,47));
var strKeyPress7	= new Array(new Array(65,90), new Array(97,122));
var strKeyPress8	= new Array(new Array(47,57));
var strKeyPress9	= new Array(new Array(65,90), new Array(97,122), new Array(32,32));
var strKeyPress10	= new Array(new Array(65,90), new Array(97,122), new Array(32,32), new Array(38,38), new Array(40,47), new Array(58,58), new Array(64,64));// Change by MTEA, 21/03/2008
var strKeyPress11	= new Array(new Array(48,58));
var strKeyPress12	= new Array(new Array(65,90), new Array(97,122), new Array(32,32), new Array(38,38), new Array(40,47), new Array(58,58), new Array(64,64), new Array(39,39));
var strKeyPress13	= new Array(new Array(65,90), new Array(97,122), new Array(48,57), new Array(32,32), new Array(33,33), new Array(35,38), new Array(40,47), new Array(58,59), new Array(61,61), new Array(91,91), new Array(93,95), new Array(123,126));
var strKeyPress14	= new Array(new Array(65,90), new Array(97,122), new Array(48,57), new Array(32,32), new Array(45,45));
var strKeyPress15	= new Array(new Array(64,90), new Array(97,122), new Array(32,32), new Array(46,47));
var strKeyPress16	= new Array(new Array(65,90), new Array(97,122), new Array(48,57), new Array(32,32), new Array(46,46));
var strKeyPress17	= new Array(new Array(65,90), new Array(97,122), new Array(32,32), new Array(40,41), new Array(45,45), new Array(47,47));
var strKeyPress18	= new Array(new Array(33,33), new Array(35,38), new Array(40,47), new Array(58,59), new Array(61,61), new Array(91,91), new Array(93,95), new Array(123,126));
var strKeyPress19	= new Array(new Array(65,90), new Array(97,122), new Array(48,57), new Array(32,32), new Array(38,38), new Array(40,47), new Array(58,58), new Array(64,64), new Array(39,39));
var strKeyPress20	= new Array(new Array(49,57));
var strKeyPress21	= new Array(new Array(66,66), new Array(75,75), new Array(77,77), new Array(98,98), new Array(107,107), new Array(109,109), new Array(48,57), new Array(46,46), new Array(44,44));
var strKeyPress22	= new Array(new Array(65,90), new Array(97,122), new Array(48,57));
var strKeyPress23	= new Array(new Array(48,57), new Array(46,46));
var strKeyPress24	= new Array(new Array(48,57), new Array(65,90), new Array(95,95), new Array(97,122));
var strKeyPress25	= new Array(new Array(65,90), new Array(97,122), new Array(32,32), new Array(46,46));
var strKeyPress26	= new Array(new Array(48,57), new Array(45,45));
var strKeyPress27	= new Array(new Array(32,32), new Array(47,57), new Array(58, 58), new Array(65, 65), new Array(77, 77), new Array(80, 80));
var strKeyPress28	= new Array(new Array(48,57), new Array(45,45), new Array(46,46));
var strKeyPress29	= new Array(new Array(48,57), new Array(45,45), new Array(32,32));
var strKeyPress30   = new Array(new Array(64,90), new Array(97,122), new Array(48,57), new Array(46,47), new Array(95,95));
var strKeyPress31   = new Array(new Array(48,57), new Array(44,45), new Array(42,42), new Array(47,47));
var strKeyPress32   = new Array(new Array(48,57), new Array(44,45), new Array(42,42), new Array(47,47), new Array(66,66), new Array(63,63), new Array(76,76), new Array(87,87));
var strKeyPress33   = new Array(new Array(64,90), new Array(97,122), new Array(48,57), new Array(32,32), new Array(93,93), new Array(46,47), new Array(95,95)); // ADD BY MTEA, 09/01/2008
//var strKeyPress34   = new Array(new Array(64,90), new Array(97,122), new Array(48,57), new Array(32,32), new Array(38,38), new Array(46,47), new Array(95,95)); // ADD BY MTEA, 09/01/2008
//var strKeyPress34   = new Array(new Array(64,90), new Array(97,122), new Array(48,57), new Array(32,32), new Array(38,38), new Array(46,47), new Array(95,95), new Array(39,39), new Array(45,45), new Array(33,33)); // CHG BY MTEA, 28/01/2008,21/03/2008
var strKeyPress34   = new Array(new Array(65,90), new Array(97,122), new Array(48,57), new Array(32,32), new Array(38,38), new Array(46,47), new Array(95,95), new Array(39,39), new Array(45,45), new Array(33,33), new Array(64,64), new Array(40,41)); // CHG BY MTEA, 24/03/2008, 03/04/2008
var strKeyPress35	= new Array(new Array(65,90), new Array(97,122), new Array(48,57), new Array(32,32), new Array(33,33), new Array(35,38), new Array(40,47), new Array(58,59), new Array(61,61), new Array(91,91), new Array(93,95), new Array(123,123), new Array(125,126)); // ADD BY JOE, 11022008
var strKeyPress36	= new Array(new Array(48,57), new Array(44,44), new Array(35,35)); //ADDED BY JOE, 12022008, CHG BY LH 30042008
var strKeyPress37   = new Array(new Array(65,90), new Array(97,122), new Array(48,57), new Array(64,64), new Array(45,45), new Array(95,95), new Array(46,46));
var strKeyPress38   = new Array(new Array(65,90), new Array(97,122), new Array(48,57), new Array(46,46), new Array(47,47), new Array(45,45));
var strKeyPress39   = new Array(new Array(65,90), new Array(97,122), new Array(48,57), new Array(33,47), new Array(58,64), new Array(91,96), new Array(123,126));

var strKeyPressErr1		= 'A to Z, a to z, 0 to 9, .';
var strKeyPressErr2		= 'A to Z, a to z, 0 to 9, spaces';
var strKeyPressErr3		= '0 to 9';
var strKeyPressErr4		= 'A to Z, a to z, -';
var strKeyPressErr5		= 'A to Z, a to z, spaces, (, ), /';
var strKeyPressErr6		= 'A to Z, a to z, (, ), ., /';
var strKeyPressErr7		= 'A to Z, a to z';
var strKeyPressErr8		= '0 to 9, /';
var strKeyPressErr9		= 'A to Z, a to z, spaces';
//var strKeyPressErr10	= 'A to Z, a to z, spaces, &, (, ), *, +, ,, -, ., /, :';
var strKeyPressErr10	= 'A to Z, a to z, spaces, &, (, ), *, +, ,, -, ., /, :, @'; //ADD BY MTEA, 21/03/2008
var strKeyPressErr11	= '0 to 9, :';
var strKeyPressErr12	= 'A to Z, a to z, spaces, &, (, ), *, +, ,, -, ., /, :, @, \'';
var strKeyPressErr13	= 'A to Z, a to z, 0 to 9, spaces, !, #, $, %, &, (, ), *, +, ,, -, ., /, :, ;, =, ?, @, [, ], ^, _, {, |, }, ~';
var strKeyPressErr14	= 'A to Z, a to z, 0 to 9 , space , -';
var strKeyPressErr15	= 'A to Z, a to z, spaces, @, /, .';
var strKeyPressErr16	= 'A to Z, a to z, 0 to 9, . , spaces';
var strKeyPressErr17	= 'A to Z, a to z, spaces, (, ), /, -';
var strKeyPressErr18	= '!, #, $, %, &, (, ), *, +, ,, -, ., /, :, ;, =, ?, @, [, ], ^, _, {, |, }, ~';
var strKeyPressErr19	= 'A to Z, a to z, 0 to 9, spaces, &, (, ), *, +, ,, -, ., /, :, @, \'';
var strKeyPressErr20	= '1 to 9';
var strKeyPressErr21	= 'K, M, B, k, m, b, 0 to 9, ., ,';
var strKeyPressErr22	= 'A to Z, a to z, 0 to 9';
var strKeyPressErr23	= '0 to 9, .';
var strKeyPressErr24	= 'A to Z, a to z, 0 to 9, _';
var strKeyPressErr25	= 'A to Z, a to z, spaces, /, .';
var strKeyPressErr26	= '0 to 9, -';
var strKeyPressErr27	= '0 to 9, / : AM/PM';
var strKeyPressErr28	= '0 to 9, -, .';
var strKeyPressErr29	= '0 to 9, -, spaces';
var strKeyPressErr30	= 'A to Z, a to z, 0 to 9, @, /, ., _';
var strKeyPressErr31 	= '0 to 9, ,, -, *, /';
var strKeyPressErr32 	= '0 to 9, ,, -, *, /, ?, L, W';
var strKeyPressErr33	= 'A to Z, a to z, 0 to 9, spaces, @, /, ., _'; // ADD BY MTEA, 09/01/2008
//var strKeyPressErr34	= 'A to Z, a to z, 0 to 9, spaces, &, /, ., _'; // ADD BY MTEA, 09/01/2008
//var strKeyPressErr34	= 'A to Z, a to z, 0 to 9, spaces, &, /, ., _, \''; // CHG BY MTEA, 28/01/2008
var strKeyPressErr34	= 'A to Z, a to z, 0 to 9, spaces, &, /, ., _, \', -, !, @, (, )'; //CHG BY MTEA, 21/03/2008, 03/04/2008
var strKeyPressErr35	= 'A to Z, a to z, 0 to 9, spaces, !, #, $, %, &, (, ), *, +, ,, -, ., /, :, ;, =, ?, @, [, ], ^, _, {, }, ~'; // ADD BY JOE, 11022008
var strKeyPressErr36 	= '0 to 9, ,, #'; // ADDED BY JOE, 12/02/2008
var strKeyPressErr37 	= 'A to Z, a to z, 0 to 9, @, -, _, .'; //For email validation
var strKeyPressErr38 	= 'A to Z, a to z, 0 to 9, ., /, -'; //For YTL Comms Subscriber Account No
var strKeyPressErr39    = 'A to Z, a to z, 0 to 9, ~, `, !, @, #, $, %, ^, &, *, (, ), -, _, +, =, {, [, }, ], |, \\, :, ;, ", \', ?, /, >, ., <, ,'; //All characters except SPACES

function ValidateKeyPress(evt, intChecking){
  var keyValue;
  keyValue = document.all ? evt.keyCode : evt.which ;
  strValidate = CONST_BYPASS_KEYCODE;
  for (var i=0; i< eval('strKeyPress' + intChecking).length; i++){
	var checkValue = eval('strKeyPress' + intChecking)[i];
  	strValidate += ' || (keyValue >= ' + checkValue[0] + ' && keyValue <= ' + checkValue[1] + ')';
  }
  if (eval(strValidate)){
  	return true;
  } else {
  	alert('Valid value : ' + eval('strKeyPressErr' + intChecking)); // CHG BY MTEA, 03/04/2008 (Valid Value -> Valid value)
  	return false;
  }
}

//validate format when save
function ValidateFormatOnSave(obj, objDesc, intChecking){
	var alphaNum = obj.value;
	strValidate = CONST_BYPASS_KEYCODE;
	for (var j=0; j < alphaNum.length; j++){
		var keyValue = alphaNum.charCodeAt(j);
		for (var i=0; i< eval('strKeyPress' + intChecking).length; i++){
			var checkValue = eval('strKeyPress' + intChecking)[i];
  			strValidate += ' || (keyValue >= ' + checkValue[0] + ' && keyValue <= ' + checkValue[1] + ')';
  		}
  		if (!eval(strValidate)){
		 	ErrorFocus('Valid value for ' + objDesc + ': ' + eval('strKeyPressErr' + intChecking), obj);
		  	return false;
  		}
	}
	return true;
}

//display error msgbox. Then focus on the object
function ErrorFocusChkEmpty(strMsgDesc,objFocus) {
 alert(strMsgDesc);
 if (objFocus != null) {
  if (!objFocus.disabled)
   objFocus.focus();
 }
}

//Edit box must have value
function ChkEmptyEditBox(objValue,objFocus,strMsgDesc) {
	if (Trim(objValue.value) == '') {
		if(objFocus == null) objFocus = objValue;
  		ErrorFocusChkEmpty ('Please enter ' +strMsgDesc,objFocus);
  		return false;
 	}
 	return true;
}

//Select/listbox must have value
function ChkEmptySelect(objValue,objFocus,strMsgDesc) {
	if (Trim(objValue.value) == '') {
  		if(objFocus == null) objFocus = objValue;
		ErrorFocus('Please select ' +strMsgDesc,objFocus);
		return false;
	}
	return true;
}

//listbox must have value
function ChkEmptyListBox(objValue,objFocus,strMsgDesc) {
	if (objValue.options.length == 0) {
		ErrorFocus(strMsgDesc,objFocus);
		return false;
	}
	return true;
}

//Radio must be checked
function ChkEmptyChecked(objValue,objFocus,strMsgDesc){
	var val = getSelectedRadioValue(objValue);
	if(val.length==0){
		if(objFocus == null) objFocus = objValue;
		ErrorFocusRadioUncheck('Please check ' +strMsgDesc,objFocus);
		return false;
	}
	else{
		return true;
	}
}

//check valid integer/numeric with zero
//bAllowZero	- 'N': value > 0
//				- 'Y': value >= 0
//iDecimal		- number of decimal
//iFloat		- number of floating point
function ChkNumberZero(objValue,objFocus,strMsgDesc,bAllowZero,iDecimal,iFloat) {
	var sZero,sValue
	sValue = Trim(objValue.value)

	if (ChkNumberFormat(objValue,objFocus,strMsgDesc,iDecimal,iFloat)) {
		sValue = FormatNumber(sValue,parseInt(iFloat), false);
		sZero = FormatNumber(0,parseInt(iFloat))
		if (bAllowZero == 'N') {	//value must be > 0
			if ((sValue - sZero) <= 0) {
				if(objFocus==null) objFocus = objValue;
				ErrorFocus(strMsgDesc +' must be greater than ' +sZero,objFocus);
				return false;
			}
		}
		else {						//value must be >= 0
			if ((sValue - sZero) < 0) {
				if(objFocus==null) objFocus = objValue;
				ErrorFocus(strMsgDesc +' must be greater than or same as ' +sZero,objFocus);
				return false;
			}
		}
	}
	else
		return false;

	objValue.value = FormatNumber(sValue,parseInt(iFloat), false);
	return true;
}

function ChkNumberNotZero(objValue,objFocus,strMsgDesc) {
	if (!validateCurrencyObject(objValue, strMsgDesc)) { return false; }
	var sValue = parseFloat(StripCurrencyFmt(Trim(objValue.value)));
	if (sValue <= 0) {
		if(objFocus==null) { objFocus = objValue; }
		ErrorFocus(strMsgDesc +' must be greater than 0',objFocus);
		return false;
	}
	return true;
}

//check valid numeric(either integer or number)
function ChkNumeric(objValue,objFocus,strMsgDesc) {
	if(isNaN(Trim(objValue.value))) {
		if(objFocus==null) { objFocus = objValue; }
		ErrorFocus(strMsgDesc +' must be numeric',objFocus);
		return false;
	}
	return true;
}

// check valid Currency numeric (numeric with comma)
// strTextboxName - textbox name
// strMsgDesc - textbox description
function validateCurrency(strTextboxName, strMsgDesc){
	return validateCurrencyObject(document.getElementById(strTextboxName), strMsgDesc);
}

function validateCurrencyObject(obj, strMsgDesc){
	var s1 = obj.value.replace(/\$|\,/g,'');
	if(isNaN(s1)) {
		ErrorFocus(strMsgDesc + ' must be numeric', obj);
		return false;
	}
	return true;
}

//check valid integer/numeric format
//iDecimal		- number of decimal
//iFloat		- number of floating point
function ChkNumberFormat(objValue,objFocus,strMsgDesc,iDecimal,iFloat) {
	var sZero,sValue,iDecimalTemp = iDecimal;
	sValue = Trim(objValue.value);

	if (!IsNumber(Trim(objValue.value),parseInt(iDecimalTemp),parseInt(iFloat))) {
		var sDesc;
		if(iDecimal==null) {
			if (parseInt(iFloat) == 0)
				sDesc = strMsgDesc +' must be in integer format';
			else
				sDesc = strMsgDesc +' must be in numeric format with ' +iFloat +' decimal point';
		} else {
			if (parseInt(iFloat) == 0)
				sDesc = strMsgDesc +' must be in integer format';
			else
				sDesc = strMsgDesc +' must be in numeric format (' +RepeatString(iDecimalTemp,'9') +'.' +RepeatString(iFloat,'9') +')';
		}

		if(objFocus==null) objFocus = objValue;
		ErrorFocus(sDesc,objFocus);
		return false;
	}
	objValue.value = FormatNumber(sValue,parseInt(iFloat))
	return true;
}

//display error msgbox. Then focus on the object
function ErrorFocus(strMsgDesc,objFocus) {
	alert(strMsgDesc);
	if (Trim(objFocus.value) != '') {
		if (!objFocus.disabled){
			objFocus.focus();
		}
	}
}
function ErrorFocusRadioUncheck(strMsgDesc,objFocus){
	alert(strMsgDesc);
	objFocus.focus();
	//objFocus[0].focus();
}

function Trim(str){
   return str.replace(/^\s*|\s*$/g,"");
}

function TrimMe(field){
   var str = field.value;
   str = str.replace(/^\s*|\s*$/g,"");
   field.value=str;
}
/*
function Trim(TRIM_VALUE) {
	if (typeof TRIM_VALUE != 'string') { return TRIM_VALUE; }

	if(TRIM_VALUE.length < 1) { return''; }
	TRIM_VALUE = RTrim(TRIM_VALUE);
	TRIM_VALUE = LTrim(TRIM_VALUE);
	if(TRIM_VALUE=='') { return ''; }
	else { return TRIM_VALUE;}
}

function RTrim(VALUE) {
	var w_space = String.fromCharCode(32);
	var v_length = VALUE.length;
	var strTemp = '';
	if(v_length < 0){ return''; }
	var iTemp = v_length -1;

	while(iTemp > -1) {
		if(VALUE.charAt(iTemp) == w_space) { }
		else {
			strTemp = VALUE.substring(0,iTemp +1);
			break;
		}
		iTemp = iTemp-1;
	}

	return strTemp;
}

function LTrim(VALUE) {
	var w_space = String.fromCharCode(32);
	if(v_length < 1) { return''; }

	var v_length = VALUE.length;
	var strTemp = '';
	var iTemp = 0;

	while(iTemp < v_length) {
		if(VALUE.charAt(iTemp) == w_space) { }
		else {
			strTemp = VALUE.substring(iTemp,v_length);
			break;
		}
		iTemp = iTemp + 1;
	}
	return strTemp;
}
*/

//same as VbScript String()
function RepeatString(intCount,strValue) {
	var strRepeatString;
	strRepeatString = ''
	for(var i=0;i<intCount;i++) strRepeatString += strValue;
	return strRepeatString;
}

//validate Prc
//DigitA = Digit Allowed Before Decimal Point.
//DigitB = Digit Allowed After Decimal Point.
function IsNumber(Prc,DigitA,DigitB) {
	var DigitAfterLimitPrcSplit,bIsNumber;
	Prc = Trim(Prc);

	for (var i=0;i<Prc.length;i++) {					//Not Numeric
		if (!(Prc.charAt(i) == ',' || Prc.charAt(i) == '.' || Prc.charAt(i) == '-' || Prc.charAt(i) == '+'))
			if (isNaN(parseInt(Prc.charAt(i)))) return false;
	}

	var PrcSplit = Prc.split('.');
	if (isNaN(Prc))
		return false;									//Not Numeric
	else if (PrcSplit.length-1 == 0)					//No Decimal Point Found
		return IsNumber2(Prc,DigitA); 					//Error DigitA Error
	else {												//Decimal Point Found
		if (PrcSplit[1].length > DigitB) {
			if (!parseInt(PrcSplit[1]) == 0) {
				if (PrcSplit[1].length > DigitB) return false;	//Error DigitB Error
			}
		}
		if (Trim(PrcSplit[0]) == '') { PrcSplit[0] = '0'; }
		if (!IsNumber2(PrcSplit[0],DigitA)) return false;		//Error DigitA Error
	}
	return true;
}
function IsNumber2(InPrc,Size) {
	var x,i,TotalLen,InPrcSplit;
	var InPrcSplit = InPrc.split(',');

	if (InPrcSplit.length-1 == 0) {
		TotalLen = InPrc.length;
		if (InPrc.length > Size) return false;		//Digit A Too Long
	}
	else {
		TotalLen = 0;
		for(i=0;i<InPrcSplit.length;i++) {
			if(i != 0) {
				x = InPrcSplit[i].length % 3;
				if(x != 0) return false;			//Comma Does Not Put Correctly
			}
			TotalLen = TotalLen + InPrcSplit[i].length;
		}
	}
	if(TotalLen > Size) return false;

	return true;
}

function ClearListbox(list) {
	list.options.length = 0;
}

function ClearList(strListName){
	ClearListbox(document.getElementById(strListName));
}

//to close child window when parent window is closed
function CloseChildWin(winArray) {
	for(i=0;i<winArray.length;i++) winArray[i].close();
	winArray.length = 0;
}

//accepts only integer: no leading 0s allowed
function validateInt(obj, strErrMsg) {
	var objValue = Trim(obj.value);
	if 	(objValue=='') return false;
	if (('' + parseInt(objValue)) == objValue) return true;
	else {
   		ErrorFocus(strErrMsg + ' must be of integer value', obj);
   		return false;
   }
}

//FORMAT CURRENCY WHEN TEXTBOX LOST FOCUS.
//curTxtBox	  	- formName.txtboxName
//preDecimal   	- required digit b4 decimal point
//postDecimal  	- required digit after decimal point
//flag		  	- TRUE: check minimum amount
//minAmt		- minimum amount
//callFunction 	- execute additional validation
//canNegative	- specify whether allow negative values
function FmtCurrency(curTxtBox,preDecimal,postDecimal,flag,minAmt,callFunction,canNegative) {

	var workNum = curTxtBox.value.trim().toUpperCase();
	if (workNum == '') { return; }
	workNum = workNum.replace(/\$|\,/g,'');

	posK = workNum.indexOf('K');
	posM = workNum.indexOf('M');
	posB = workNum.indexOf('B');
	posDec = workNum.indexOf('.');

	if (posK!=-1 || posM!=-1 || posB!=-1) {
		if ((posK!=-1 && posM!=-1) || (posK!=-1 && posB!=-1) || (posB!=-1 && posM!=-1)) {
			ErrorFocus ('Only one character (K, M, B) is allowed at a time.', curTxtBox);
			return false;
		}
	}

	var val='', multiplyBy=1, tempVal = '';
	if (posK!=-1 )      {
		val = workNum.substring(0,posK);

		if (posDec != -1){
			tempVal = val.substring(posDec + 1, posK);
			val = val.replace('.','');
		}

		if ((tempVal.length <= 3) || (tempVal == '')){
			for(i = 0; i < 3 - tempVal.length ; i ++)
				val = val + '0';
		}
		else
			val = val.substring(0,posDec) + val.substring(posDec, posDec + 3 ) + '.' + val.substring(posDec + 3);

	}else if (posM!=-1) {

		val = workNum.substring(0,posM);
		if (posDec != -1){
			tempVal = val.substring(posDec + 1, posM);
			val = val.replace('.','');
		}
		if ((tempVal.length <= 6) || (tempVal == '')){
			for(i = 0; i < 6 - tempVal.length ; i ++)
				val = val + '0';
		}
		else
			val = val.substring(0,posDec) + val.substring(posDec, posDec + 6 ) + '.' + val.substring(posDec + 6);

	}else if (posB!=-1) {
		val = workNum.substring(0,posB);

		if (posDec != -1){
			tempVal = val.substring(posDec + 1, posB);
			val = val.replace('.','');
		}

		if ((tempVal.length <= 9) || (tempVal == '')){
			for(i = 0; i < 9 - tempVal.length ; i ++)
				val = val + '0';
		}
		else
			val = val.substring(0,posDec) + val.substring(posDec, posDec + 9 ) + '.' + val.substring(posDec + 9);
	}

	if (val.length>0 && !isNaN(val)) { 	workNum = val; 	}

	if (val.length>0)
		posDec = val.indexOf('.');

	if (posDec == -1)
		workNum = workNum + '.00'

	var workNumPre = '', fstDigit = '', blnRemove = 'false';
	if (workNum.length>=21){
		workNumPre = workNum.substring(0, workNum.length - 21);
		workNum = workNum.substring(workNum.length - 21);
		fstDigit = workNum.substring(0,1);
		wnLength = workNum.length;

		if (fstDigit == '0'){
			workNum = '1' + workNum;
			blnRemove = 'true';
			fstDigit = '1';
		}
	}

	if (isNaN(workNum)) { ErrorFocus ('Please key in numeric value or numeric value with character K, M or B only',curTxtBox); return; }

	curTxtBox.value = workNumPre + workNum;

	if (!ChkNumberFormat(curTxtBox,curTxtBox,'Amount',preDecimal,postDecimal)) { return; }

	if (canNegative!=null) {
		if (!canNegative) {	if (workNum<0) { ErrorFocus('Negative values are not allowed',curTxtBox); return; }	}
	}

	if (flag!=null && minAmt!=null) {
		if (flag.trim().toUpperCase() == 'TRUE') {
			if (parseFloat(workNum) < parseFloat(minAmt)) {
				ErrorFocus ('Amount must be greater than ' +FormatNumber(minAmt,parseInt(postDecimal)),curTxtBox);
				return;
			}
		}
	}

	if (parseFloat(preDecimal) > 13) {
		var PrcSplit = workNum.split('.');

		if (postDecimal == 0) {
			workNum = Add$Comma(PrcSplit[0]);
		}
		else {
			PrcSplit[0] = FormatNumber(PrcSplit[0],0);

			PrcSplit[1] = PrcSplit[1] + RepeatString(postDecimal-PrcSplit[1].length,'0');
			workNum = Add$Comma(PrcSplit[0] +'.'+ PrcSplit[1]);
		}
	}
	else {
		workNum = Add$Comma(FormatNumber(workNum, postDecimal));
	}


	if (workNumPre != ''){
		if (workNum.replace(/\$|\,/g,'').length != wnLength){
			if (blnRemove =='false')
				workNumPre = parseInt(workNumPre) + 1;

			workNumPre = Add$Comma(workNumPre);
			curTxtBox.value = workNumPre + ',' + workNum.substring(2);
		}else{
			workNumPre = Add$Comma(workNumPre);
			if (blnRemove == 'true')
				curTxtBox.value = workNumPre + ',' + workNum.substring(2);
			else
				curTxtBox.value = workNumPre + ',' + workNum;
		}
	}
	else
		curTxtBox.value = workNum;


	if (callFunction!=null) { eval(callFunction); }
	return true;
}

function FmtScrNumber(sNumber,decimal) {
	if (sNumber=='') { return; }
	else return Add$Comma(FormatNumber(sNumber, decimal));
}

function Add$Comma(num) {
	var arr, dollar='', cents='', minus='';
	var num = new String(num);
	arr = num.split('.');
	dollar = arr[0].replace(/\$|\,/g,'');

	if (arr.length>1) {	cents = arr[1]; }
	if (parseFloat(dollar)<0) {
		minus = '-';
		dollar = dollar.substr(1);
	}

	for (var i=0; i<Math.floor((dollar.length-(1+i))/3); i++) {
		dollar = dollar.substring(0, dollar.length-(4*i+3)) +','+ dollar.substring(dollar.length-(4*i+3));
	}

	return (cents=='') ? minus +dollar : minus +dollar +'.' +cents;
}

function StripCurrencyFmt(num) {
	num = (isNaN(parseFloat(num))==true) ? '0' : num;
	var blnSign = (parseFloat(num)>=0) ? '' : '-';
	return num.replace(/\$|\,/g,'');
	//return blnSign + num.replace(/\$|\,/g,'');
	//return blnSign + Math.abs(num.replace(/\$|\,/g,''));
}

function ValidatePercentage(obj) {
	ValidatePercentageWithNDecimnal(obj, 2);
}

function ValidatePercentageWithNDecimnal(obj, intNDecimal){
	var sPerc = obj.value;
	if (sPerc == '') {
		return;
	} else {
		if (IsNumber(sPerc,3,intNDecimal)) {
			sPerc = FormatNumber(sPerc,intNDecimal);
			if (sPerc < 0.00 || sPerc > 100.00) {
				alert('Invalid Percentage.');
				obj.focus();
			} else {
				obj.value = sPerc;
			}
		} else {
			alert('Invalid Percentage Format');
			obj.focus();
		}
	}
}

String.prototype.trim=function()  { return this.replace(/^\s+|\s+$/g,''); }

String.prototype.ltrim=function() { return this.replace(/^\s+/g,''); }

String.prototype.rtrim=function() { return this.replace(/\s+$/g,''); }

function trimLeadingZeros(str) {
	return str.replace(/^0*/,''); //0 at the beginning, one or more
}

function removeWhiteSpaceByObj(obj){
	obj.value = removeWhiteSpace(obj.value);
}

function removeWhiteSpace(strValue){
	return strValue.replace(' ', '');
}

function replaceSubstring(strValue,expression,value){
	var exp = new RegExp(expression,value);
	return strValue.replace(exp,value);
}

function ShowToolTip(src,tip) {	src.title = tip; return; }
function HideToolTip(src) {	/**src.style.display = none; **/ }

//common function to popup a window
function PopupWindow(Link, sWindowArray, width, height) {
	if (width=='')  width = parseInt(screen.availWidth-100);
	if (height=='') height = parseInt(screen.availHeight-100);
	var x = Math.round((screen.availWidth - width) / 2);
	var y = Math.round((screen.availHeight - height) / 2);
	var winSetting = 'left='+x+',top='+y+',width='+width+',height='+height+',scrollbars=yes,resizable=no,toolbar=no,titlebar=yes,location=no,directories=no,status=yes,menubar=no,resizable=no,copyhistory=no';

	var hWnd = window.open(Link,'hWnd',winSetting);
	sWindowArray[sWindowArray.length] = hWnd;
	if ((document.window != null) && (!hWnd.opener)) { hWnd.opener = document.window;}
	hWnd.focus();
}

//trimLength and limitChars is used together for limiting the number of characters in textarea
//This function is necessary in case some text is pasted in.
//Use with Event onchange
function trimLength(textarea, maxChars){
	if(textarea.value.length <= maxChars) return;
	textarea.value = textarea.value.substr(0, maxChars)
}

//Returning false makes the borwser ignore the key press, and hence not put the character into the <textarea>.
//Use with Event onkeypress
function limitChars(textarea, maxChars) {
	if(typeof(textarea.onkeypress.arguments[0]) != 'undefined')
		var keyCode = textarea.onkeypress.arguments[0].keyCode;
	else {
		if(document.selection.createRange().text.length != 0) return true;
		var keyCode = event.keyCode;
	}

	var allowedChars = new Array(8, 37, 38, 39, 40, 46);	//Backspace, delete and arrow keys
	for(var x=0; x<allowedChars.length; x++) if(allowedChars[x] == keyCode) return true;

	if(textarea.value.length < maxChars) return true;

	return false;
}

function removeHTMLTag(objElement){
	return removeHTMLTagValue(objElement.innerHTML);
}

function removeHTMLTagValue(strElement){
	return strElement.replace(/<[^>]+>/g,'');
}

function FormatNumberWithComma(pnumber,decimals,IsNegative) {
	var amount = FormatNumber(pnumber,decimals,IsNegative);

	var decimal_location = amount.indexOf(".")
	var a,d,i;
	if (decimal_location == -1) {
		d = "";
		i = amount;
	} else {
		a = amount.split('.',2)
		d = a[1];
		i = parseInt(a[0]);
	}


	var minus = '';
	if(i < 0) { minus = '-'; }
	i = Math.abs(i);
	var delimiter = ',';
	var a = [];
	var n = new String(i);
	while(n.length > 3) {
		var nn = n.substr(n.length-3);
		a.unshift(nn);
		n = n.substr(0,n.length-3);
	}

	if(n.length > 0) { a.unshift(n); }
	n = a.join(delimiter);
	if(d.length < 1) { amount = n; }
	else { amount = n + '.' + d; }
	amount = minus + amount;
	return amount;
}

function FormatNumber(pnumber,decimals,IsNegative) {
	return round_decimals(pnumber,decimals);
}

function round_decimals(original_number, decimals) {
    var result1 = original_number * Math.pow(10, decimals)
    var result2 = Math.round(result1)
    var result3 = result2 / Math.pow(10, decimals)

    return pad_with_zeros(result3, decimals)

}

function pad_with_zeros(rounded_value, decimal_places) {

    // Convert the number to a string
    var value_string = rounded_value.toString()

    // Locate the decimal point
    var decimal_location = value_string.indexOf(".")

    // Is there a decimal point?
    if (decimal_location == -1) {

        // If no, then all decimal places will be padded with 0s
        decimal_part_length = 0

        // If decimal_places is greater than zero, tack on a decimal point
        value_string += decimal_places > 0 ? "." : ""
    }
    else {

        // If yes, then only the extra decimal places will be padded with 0s
        decimal_part_length = value_string.length - decimal_location - 1
    }

    // Calculate the number of decimal places that need to be padded with 0s
    var pad_total = decimal_places - decimal_part_length

    if (pad_total > 0) {

        // Pad the string with 0s
        for (var counter = 1; counter <= pad_total; counter++)
            value_string += "0"
        }
    return value_string
}

function ErrorConfirm(strMsgDesc, objFocus) {
	var blnCont = !confirm(strMsgDesc);
	if (blnCont == false) {
		objFocus.focus();
	}
	return blnCont;
}

/** TO VALIDATE PASSWORD **/
function ChkPassword(objValue1,objValue2,objValue3){
	var minLength = 8; // Minimum length

	if(!ChkEmptyEditBox(objValue1, objValue1, "Old Password")) return false;
	if(!ChkEmptyEditBox(objValue2, objValue2, "New Password")) return false;
	if(!ChkEmptyEditBox(objValue3, objValue3, "Confirmed New Password")) return false;
	if(!ValidateFormatOnSave(objOldPwd, "Old Password", 22)) return false;
	if(!ValidateFormatOnSave(objNewPwd, "New Password", 22)) return false;
	if(!ValidateFormatOnSave(objCfmNewPwd, "Confirmed New Password", 22)) return false;
	var value2 = Trim(objValue2.value);
	var value3 = Trim(objValue3.value);
	if(value2.length<minLength){
		var strMsgDesc = "New Password must be at least "+minLength +" alphanumeric characters";
  		ErrorFocusChkEmpty (strMsgDesc,objValue2);
  		return false;
	}
	else{
		if(value3.length<minLength){
			var strMsgDesc = "Confirmed New Password must be at least "+minLength +" alphanumeric characters";
  			ErrorFocusChkEmpty (strMsgDesc,objValue3);
  			return false;
		}
		else{
			if(Trim(objValue2.value) != Trim(objValue3.value)){
				var strMsgDesc = "Confirmed New Password and New Password is incorrect";
  				ErrorFocusChkEmpty (strMsgDesc,objValue3);
  				return false;
			}
		}
	}
	return true;
}

function ChkPasswordMinLength(objValue1,objValue2,objValue3,minLength){
	//var minLength = 8; // Minimum length

	if(!ChkEmptyEditBox(objValue1, objValue1, "Old Password")) return false;
	if(!ChkEmptyEditBox(objValue2, objValue2, "New Password")) return false;
	if(!ChkEmptyEditBox(objValue3, objValue3, "Confirmed New Password")) return false;
	var value2 = Trim(objValue2.value);
	var value3 = Trim(objValue3.value);
	if(value2.length<minLength){
		var strMsgDesc = "New Password must be at least "+minLength +" alphanumeric characters";
  		ErrorFocusChkEmpty (strMsgDesc,objValue2);
  		return false;
	}
	else{
		if(value3.length<minLength){
			var strMsgDesc = "Confirmed New Password must be at least "+minLength +" alphanumeric characters";
  			ErrorFocusChkEmpty (strMsgDesc,objValue3);
  			return false;
		}
		else{
			if(Trim(objValue2.value) != Trim(objValue3.value)){
				var strMsgDesc = "Confirmed New Password and New Password is incorrect";
  				ErrorFocusChkEmpty (strMsgDesc,objValue3);
  				return false;
			}
		}
	}
	return true;
}
/** TO VALIDATE PASSWORD **/

function getSelectedRadio(buttonGroup) {
   // returns the array number of the selected radio button or -1 if no button is selected
   if (buttonGroup[0]) { // if the button group is an array (one button is not an array)
      for (var i=0; i<buttonGroup.length; i++) {
         if (buttonGroup[i].checked) {
            return i
         }
      }
   } else {
      if (buttonGroup.checked) { return 0; } // if the one button is checked, return zero
   }
   // if we get to this point, no radio button is selected
   return -1;
} // Ends the "getSelectedRadio" function
function getSelectedRadioValue(buttonGroup) {
   // returns the value of the selected radio button or "" if no button is selected
   var i = getSelectedRadio(buttonGroup);
   if (i == -1) {
      return "";
   } else {
      if (buttonGroup[i]) { // Make sure the button group is an array (not just one button)
         return buttonGroup[i].value;
      } else { // The button group is just the one button, and it is checked
         return buttonGroup.value;
      }
   }
} // Ends the "getSelectedRadioValue" function
function resetCheckedValue(radioObj){
	if(!radioObj){
	}else{
		var radioLength = radioObj.length;
		if(radioLength == undefined){
			if(radioObj.checked){
				radioObj.checked = false;
			}else{
			}
		}
		for(var i = 0; i < radioLength; i++) {
			if(radioObj[i].checked) {
				radioObj[i].checked = false;
			}
		}
	}
}

//[Joe 18/02/2008]
function validate_email(str){
	str = Trim(str);
	if(str.length!=0){
		return (str.indexOf(".") > 2) && (str.indexOf("@") > 0);
	}else{
		return true;
	}
}

function trimSpace(field){
   var val = field.value;
   if (val.length>0){
	 var rtn='';
     for (var i=0; i<val.length;i++){
        if (val.charAt(i)!=' '){
           rtn=rtn+val.charAt(i);
        }
     }
     val=rtn;
     field.value=val;
   }
}

function checkTextareaMaxLength_onSubmit(objTextArea, maxchars, fieldName){
	if(objTextArea.value.length > maxchars){
		alert('Maximum characters in '+fieldName+ ' is '+maxchars+ ' only! \n'+
    		'Please remove '+
    		(objTextArea.value.length - maxchars)+ ' characters.');
    	objTextArea.focus();
   		return false;
   	}else{
   		return true;
   	}
}

function validateASCII(input) {
    if (!/^[\x00-\xFF]*$/.test(input.value)) {
    	alert("This field contains invalid characters: " + input.value);
    	$(input).focus().select();
    }
}

//-->
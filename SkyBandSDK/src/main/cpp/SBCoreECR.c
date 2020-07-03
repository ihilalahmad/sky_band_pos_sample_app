#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "SBCoreECR.h"
#include "Utilities.h"
#include "ECRSrc.h"

extern void hexDataPrint(char * pchHeaderString, unsigned char * pucInPutBuffer, int inNumBytes);
extern void vdParseRequestData(char *inputReqData, long long reqFields[], int *count);
extern void xorOpBtwnChars (unsigned char *pbt_Data1, int i_DataLen, int *output);
extern void ascToHexConv (unsigned char *outp, unsigned char *inp, int iLength);
extern char *getCommand(int tranType);

EXPORT void pack(char *inputReqData, int transactionType, char *szSignature, char *szEcrBuffer)
{
	long long lnReqFields[REQFIELD_SIZE];
	int inFieldsCount = 0, inReqPacketIndex = 0, inLCR = 0;
	char szLCR[LCRBUFFER_SIZE], szLCR_Hex[LCRBUFFER_SIZE];
	char szAmountField[AMT_SIZE+1];
	char szCashbackAmountField[AMT_SIZE+1];
	char szRefundAmountField[AMT_SIZE+1];
	char szDateTimeStamp[DATETIME_SIZE+1];
	char szECRRefNum[REFNUM_SIZE+1];
	char szRcptPrntFlag[PRNTFLAG_SIZE+1];
	char sTimeout[TIMEOUT_SIZE+1] = TIMEOUT_VAL;
	char szRRNField[RRN_SIZE+1];
	char szOrigTranDate[DATE_SIZE+1];
	char szOrigApprCode[APPRCODE_SIZE+1];
	char szPartialComp[PARTIALCOMP_SIZE+1];
	char szLanguage[LANG_SIZE+1];
	char szVendorID[VENDORID_SIZE+1];
	char szVendorTermType[TERMTYPE_SIZE+1];
	char szTRSMID[TRSMID_SIZE+1];
	char szVendorKeyIndex[KEYINDEX_SIZE+1];
	char szSAMAKeyIndex[KEYINDEX_SIZE+1];
	char szCashRegNum[CASHREGNUM_SIZE+1];
	char szBillerID[BILLERID_SIZE+1];
	char szBillNum[BILLNUM_SIZE+1];
	char szPrevECRNum[ECRNUM_SIZE+1];
	char szReqAttemptNum[REQATTEMPTNUM_SIZE+1];

	vdParseRequestData(inputReqData, lnReqFields, &inFieldsCount);

	//STX ("02" Hex)
	memcpy(szEcrBuffer, STX, STX_SIZE);
	inReqPacketIndex++;
	memcpy(&szEcrBuffer[inReqPacketIndex], FIELD_SEPERATOR, FIELDSEP_SIZE); // Field separator
	inReqPacketIndex++;

	//Command
	memcpy(&szEcrBuffer[inReqPacketIndex], getCommand(transactionType), CMD_SIZE);
	inReqPacketIndex += CMD_SIZE;
	memcpy(&szEcrBuffer[inReqPacketIndex], FIELD_SEPERATOR, FIELDSEP_SIZE); // Field separator
	inReqPacketIndex++;

	if((transactionType != TYPE_RECONCILATION) && (transactionType != TYPE_REGISTER) && (transactionType != TYPE_REFUND)
	   && (transactionType != TYPE_PRECOMP) && (transactionType != TYPE_PREAUTH_EXT) && (transactionType != TYPE_PREAUTH_VOID)
	   && (transactionType != TYPE_PARAM_DOWNLOAD) && (transactionType != TYPE_GET_PARAM) && (transactionType != TYPE_SET_TERM_LANG)
	   && (transactionType != TYPE_SET_PARAM) && (transactionType != TYPE_REVERSAL) && (transactionType != TYPE_START_SESSION)
	   && (transactionType != TYPE_END_SESSION) && (transactionType != TYPE_PRNT_DETAIL_RPORT) && (transactionType != TYPE_PRNT_SUMMARY_RPORT)
	   && (transactionType != TYPE_REPEAT) && (transactionType != TYPE_CHECK_STATUS))
	{
		if(transactionType == TYPE_BILL_PAY)
		{
			memset(szBillerID, 0x00, sizeof(szBillerID));
			sprintf(szBillerID, "%06lld", lnReqFields[2]);
			memcpy(&szEcrBuffer[inReqPacketIndex], szBillerID, BILLERID_SIZE); // Biller Id
			inReqPacketIndex += BILLERID_SIZE;
			memcpy(&szEcrBuffer[inReqPacketIndex], FIELD_SEPERATOR, FIELDSEP_SIZE); // Field separator
			inReqPacketIndex++;

			memset(szBillNum, 0x00, sizeof(szBillNum));
			sprintf(szBillNum, "%06lld", lnReqFields[3]);
			memcpy(&szEcrBuffer[inReqPacketIndex], szBillNum, BILLNUM_SIZE); // Bill Number
			inReqPacketIndex += BILLNUM_SIZE;
			memcpy(&szEcrBuffer[inReqPacketIndex], FIELD_SEPERATOR, FIELDSEP_SIZE); // Field separator
			inReqPacketIndex++;
		}

		memset(szAmountField, 0x00, sizeof(szAmountField));
		sprintf(szAmountField, "%012lld", lnReqFields[1]);
		memcpy(&szEcrBuffer[inReqPacketIndex], szAmountField, AMT_SIZE); // Transaction Amount
		inReqPacketIndex += AMT_SIZE;
		memcpy(&szEcrBuffer[inReqPacketIndex], FIELD_SEPERATOR, FIELDSEP_SIZE); // Field separator
		inReqPacketIndex++;

		if(transactionType == TYPE_PURCHASE_CASHBACK)
		{
			memset(szCashbackAmountField, 0x00, sizeof(szCashbackAmountField));
			sprintf(szCashbackAmountField, "%012lld", lnReqFields[2]);
			memcpy(&szEcrBuffer[inReqPacketIndex], szCashbackAmountField, AMT_SIZE); // Cash Back Amount
			inReqPacketIndex += AMT_SIZE;
			memcpy(&szEcrBuffer[inReqPacketIndex], FIELD_SEPERATOR, FIELDSEP_SIZE); // Field separator
			inReqPacketIndex++;
		}
	}

	//Date Time Stamp
	memset(szDateTimeStamp, 0x00, sizeof(szDateTimeStamp));
	sprintf(szDateTimeStamp, "%012lld", lnReqFields[0]);
	memcpy(&szEcrBuffer[inReqPacketIndex], szDateTimeStamp, DATETIME_SIZE);
	inReqPacketIndex += DATETIME_SIZE;
	memcpy(&szEcrBuffer[inReqPacketIndex], FIELD_SEPERATOR, FIELDSEP_SIZE); // Field separator
	inReqPacketIndex++;

	if(transactionType == TYPE_PRNT_SUMMARY_RPORT)
	{
		//Request Attempted Number
		memset(szReqAttemptNum, 0x00, sizeof(szReqAttemptNum));
		sprintf(szReqAttemptNum, "%03lld", lnReqFields[1]);
		memcpy(&szEcrBuffer[inReqPacketIndex], szReqAttemptNum, REQATTEMPTNUM_SIZE);
		inReqPacketIndex += REQATTEMPTNUM_SIZE;
		memcpy(&szEcrBuffer[inReqPacketIndex], FIELD_SEPERATOR, FIELDSEP_SIZE); // Field separator
		inReqPacketIndex++;
	}

	if((transactionType == TYPE_REGISTER) || (transactionType == TYPE_START_SESSION) || (transactionType == TYPE_END_SESSION))
	{
		//Cash Register Number
		memset(szCashRegNum, 0x00, sizeof(szCashRegNum));
		sprintf(szCashRegNum, "%08lld", lnReqFields[1]);
		memcpy(&szEcrBuffer[inReqPacketIndex], szCashRegNum, CASHREGNUM_SIZE);
		inReqPacketIndex += CASHREGNUM_SIZE;
		memcpy(&szEcrBuffer[inReqPacketIndex], FIELD_SEPERATOR, FIELDSEP_SIZE); // Field separator
		inReqPacketIndex++;
	}

	if((transactionType == TYPE_REFUND) || (transactionType == TYPE_PRECOMP)
	   || (transactionType == TYPE_PREAUTH_EXT) || (transactionType == TYPE_PREAUTH_VOID) || (transactionType == TYPE_REVERSAL))
	{
		if((transactionType != TYPE_PREAUTH_EXT) && (transactionType != TYPE_REVERSAL))
		{
			//Refund Amount
			memset(szRefundAmountField, 0x00, sizeof(szRefundAmountField));
			sprintf(szRefundAmountField, "%012lld", lnReqFields[1]);
			memcpy(&szEcrBuffer[inReqPacketIndex], szRefundAmountField, AMT_SIZE);
			inReqPacketIndex += AMT_SIZE;
			memcpy(&szEcrBuffer[inReqPacketIndex], FIELD_SEPERATOR, FIELDSEP_SIZE); // Field separator
			inReqPacketIndex++;
		}

		//RRN
		memset(szRRNField, 0x00, sizeof(szRefundAmountField));
		if((transactionType == TYPE_PREAUTH_EXT) || (transactionType == TYPE_REVERSAL))
			sprintf(szRRNField, "%012lld", lnReqFields[1]);
		else
			sprintf(szRRNField, "%012lld", lnReqFields[2]);
		memcpy(&szEcrBuffer[inReqPacketIndex], szRRNField, RRN_SIZE);
		inReqPacketIndex += RRN_SIZE;
		memcpy(&szEcrBuffer[inReqPacketIndex], FIELD_SEPERATOR, FIELDSEP_SIZE); // Field separator
		inReqPacketIndex++;
	}

	if((transactionType == TYPE_PRECOMP) || (transactionType == TYPE_PREAUTH_EXT) || (transactionType == TYPE_PREAUTH_VOID))
	{
		//Original Tran Date
		memset(szOrigTranDate, 0x00, sizeof(szOrigTranDate));
		if(transactionType == TYPE_PREAUTH_EXT)
			sprintf(szOrigTranDate, "%06lld", lnReqFields[2]);
		else
			sprintf(szOrigTranDate, "%06lld", lnReqFields[3]);
		memcpy(&szEcrBuffer[inReqPacketIndex], szOrigTranDate, DATE_SIZE);
		inReqPacketIndex += DATE_SIZE;
		memcpy(&szEcrBuffer[inReqPacketIndex], FIELD_SEPERATOR, FIELDSEP_SIZE); // Field separator
		inReqPacketIndex++;

		//Original Approval Code
		memset(szOrigApprCode, 0x00, sizeof(szOrigApprCode));
		if(transactionType == TYPE_PREAUTH_EXT)
			sprintf(szOrigApprCode, "%06lld", lnReqFields[3]);
		else
			sprintf(szOrigApprCode, "%06lld", lnReqFields[4]);
		memcpy(&szEcrBuffer[inReqPacketIndex], szOrigApprCode, APPRCODE_SIZE);
		inReqPacketIndex += APPRCODE_SIZE;
		memcpy(&szEcrBuffer[inReqPacketIndex], FIELD_SEPERATOR, FIELDSEP_SIZE); // Field separator
		inReqPacketIndex++;

		if((transactionType != TYPE_PREAUTH_EXT) && (transactionType != TYPE_PREAUTH_VOID))
		{
			//Partial Completion
			sprintf(szPartialComp, "%lld", lnReqFields[5]);
			memcpy(&szEcrBuffer[inReqPacketIndex], szPartialComp, PARTIALCOMP_SIZE);
			inReqPacketIndex++;
			memcpy(&szEcrBuffer[inReqPacketIndex], FIELD_SEPERATOR, FIELDSEP_SIZE); // Field separator
			inReqPacketIndex++;
		}
	}

	if(transactionType == TYPE_SET_PARAM)
	{
		//Vendor ID
		memset(szVendorID, 0x00, sizeof(szVendorID));
		sprintf(szVendorID, "%02lld", lnReqFields[1]);
		memcpy(&szEcrBuffer[inReqPacketIndex], szVendorID, VENDORID_SIZE);
		inReqPacketIndex += VENDORID_SIZE;
		memcpy(&szEcrBuffer[inReqPacketIndex], FIELD_SEPERATOR, FIELDSEP_SIZE); // Field separator
		inReqPacketIndex++;

		//Vendor Terminal type
		memset(szVendorTermType, 0x00, sizeof(szVendorTermType));
		sprintf(szVendorTermType, "%02lld", lnReqFields[2]);
		memcpy(&szEcrBuffer[inReqPacketIndex], szVendorTermType, TERMTYPE_SIZE);
		inReqPacketIndex += TERMTYPE_SIZE;
		memcpy(&szEcrBuffer[inReqPacketIndex], FIELD_SEPERATOR, FIELDSEP_SIZE); // Field separator
		inReqPacketIndex++;

		//TRSM ID
		memset(szTRSMID, 0x00, sizeof(szTRSMID));
		sprintf(szTRSMID, "%06lld", lnReqFields[3]);
		memcpy(&szEcrBuffer[inReqPacketIndex], szTRSMID, TRSMID_SIZE);
		inReqPacketIndex += TRSMID_SIZE;
		memcpy(&szEcrBuffer[inReqPacketIndex], FIELD_SEPERATOR, FIELDSEP_SIZE); // Field separator
		inReqPacketIndex++;

		//Vendor Key Index
		memset(szVendorKeyIndex, 0x00, sizeof(szVendorKeyIndex));
		sprintf(szVendorKeyIndex, "%02lld", lnReqFields[4]);
		memcpy(&szEcrBuffer[inReqPacketIndex], szVendorKeyIndex, KEYINDEX_SIZE);
		inReqPacketIndex += KEYINDEX_SIZE;
		memcpy(&szEcrBuffer[inReqPacketIndex], FIELD_SEPERATOR, FIELDSEP_SIZE); // Field separator
		inReqPacketIndex++;

		//SAMA Key Index
		memset(szSAMAKeyIndex, 0x00, sizeof(szSAMAKeyIndex));
		sprintf(szSAMAKeyIndex, "%02lld", lnReqFields[5]);
		memcpy(&szEcrBuffer[inReqPacketIndex], szSAMAKeyIndex, KEYINDEX_SIZE);
		inReqPacketIndex += KEYINDEX_SIZE;
		memcpy(&szEcrBuffer[inReqPacketIndex], FIELD_SEPERATOR, FIELDSEP_SIZE); // Field separator
		inReqPacketIndex++;
	}

	if(transactionType == TYPE_REPEAT)
	{
		//Previous ECR Number
		memset(szPrevECRNum, 0x00, sizeof(szPrevECRNum));
		sprintf(szPrevECRNum, "%06lld", lnReqFields[1]);
		memcpy(&szEcrBuffer[inReqPacketIndex], szPrevECRNum, ECRNUM_SIZE);
		inReqPacketIndex += ECRNUM_SIZE;
		memcpy(&szEcrBuffer[inReqPacketIndex], FIELD_SEPERATOR, ECRNUM_SIZE); // Field separator
		inReqPacketIndex++;
	}

	if((transactionType != TYPE_REGISTER) && (transactionType != TYPE_START_SESSION) && (transactionType != TYPE_END_SESSION))
	{
		//ECR Transaction Reference Number
		memset(szECRRefNum, 0x00, sizeof(szECRRefNum));
		if(transactionType == TYPE_PURCHASE || transactionType == TYPE_PREAUTH || transactionType == TYPE_CASH_ADVANCE || transactionType == TYPE_REVERSAL)
			sprintf(szECRRefNum, "%14lld", lnReqFields[3]);
		else if(transactionType == TYPE_PURCHASE_CASHBACK)
			sprintf(szECRRefNum, "%14lld", lnReqFields[4]);
		else if(transactionType == TYPE_RECONCILATION || transactionType == TYPE_SET_TERM_LANG || transactionType == TYPE_REPEAT
				|| (transactionType == TYPE_PRNT_SUMMARY_RPORT))
			sprintf(szECRRefNum, "%14lld", lnReqFields[2]);
		else if((transactionType == TYPE_REFUND) || (transactionType == TYPE_PREAUTH_EXT) || (transactionType == TYPE_BILL_PAY))
			sprintf(szECRRefNum, "%14lld", lnReqFields[5]);
		else if(transactionType == TYPE_PRECOMP)
			sprintf(szECRRefNum, "%14lld", lnReqFields[7]);
		else if((transactionType == TYPE_PREAUTH_VOID) || (transactionType == TYPE_SET_PARAM))
			sprintf(szECRRefNum, "%14lld", lnReqFields[6]);
		else if((transactionType == TYPE_PARAM_DOWNLOAD) || (transactionType == TYPE_GET_PARAM) || (transactionType == TYPE_PRNT_DETAIL_RPORT)
				|| (transactionType == TYPE_CHECK_STATUS))
			sprintf(szECRRefNum, "%14lld", lnReqFields[1]);
		memcpy(&szEcrBuffer[inReqPacketIndex], szECRRefNum, REFNUM_SIZE);
		inReqPacketIndex += REFNUM_SIZE;
		memcpy(&szEcrBuffer[inReqPacketIndex], FIELD_SEPERATOR, FIELDSEP_SIZE); // Field separator
		inReqPacketIndex++;

		if(transactionType == TYPE_SET_TERM_LANG)
		{
			//Language
			memset(szLanguage, 0x00, sizeof(szLanguage));
			sprintf(szLanguage, "%lld", lnReqFields[1]);
			memcpy(&szEcrBuffer[inReqPacketIndex], szLanguage, LANG_SIZE);
			inReqPacketIndex++;
			memcpy(&szEcrBuffer[inReqPacketIndex], FIELD_SEPERATOR, FIELDSEP_SIZE); // Field separator
			inReqPacketIndex++;
		}

		if((transactionType != TYPE_PARAM_DOWNLOAD) && (transactionType != TYPE_GET_PARAM) && (transactionType != TYPE_SET_TERM_LANG)
		   && (transactionType != TYPE_SET_PARAM) && (transactionType != TYPE_PRNT_DETAIL_RPORT) && (transactionType != TYPE_PRNT_SUMMARY_RPORT)
		   && (transactionType != TYPE_REPEAT) && (transactionType != TYPE_CHECK_STATUS))
		{
			//Receipt print Flag
			if(transactionType == TYPE_PURCHASE || transactionType == TYPE_PREAUTH || transactionType == TYPE_CASH_ADVANCE || transactionType == TYPE_REVERSAL)
				sprintf(szRcptPrntFlag, "%lld", lnReqFields[2]);
			else if((transactionType == TYPE_PURCHASE_CASHBACK) || (transactionType == TYPE_REFUND))
				sprintf(szRcptPrntFlag, "%lld", lnReqFields[3]);
			else if(transactionType == TYPE_RECONCILATION)
				sprintf(szRcptPrntFlag, "%lld", lnReqFields[1]);
			else if(transactionType == TYPE_PRECOMP)
				sprintf(szRcptPrntFlag, "%lld", lnReqFields[6]);
			else if(transactionType == TYPE_PREAUTH_VOID)
				sprintf(szRcptPrntFlag, "%lld", lnReqFields[5]);
			else if((transactionType == TYPE_PREAUTH_EXT) || (transactionType == TYPE_BILL_PAY))
				sprintf(szRcptPrntFlag, "%lld", lnReqFields[4]);
			memcpy(&szEcrBuffer[inReqPacketIndex], szRcptPrntFlag, PRNTFLAG_SIZE);
			inReqPacketIndex++;
			memcpy(&szEcrBuffer[inReqPacketIndex], FIELD_SEPERATOR, FIELDSEP_SIZE); // Field separator
			inReqPacketIndex++;
		}

		//Signature
		memcpy(&szEcrBuffer[inReqPacketIndex], szSignature, SIGNATURE_SIZE);
		inReqPacketIndex += SIGNATURE_SIZE;
		memcpy(&szEcrBuffer[inReqPacketIndex], FIELD_SEPERATOR, FIELDSEP_SIZE); // Field separator
		inReqPacketIndex++;
	}

	//Time Out
	memcpy(&szEcrBuffer[inReqPacketIndex], sTimeout, TIMEOUT_SIZE);
	inReqPacketIndex += TIMEOUT_SIZE;
	memcpy(&szEcrBuffer[inReqPacketIndex], FIELD_SEPERATOR, FIELDSEP_SIZE); // Field separator
	inReqPacketIndex++;

	//ETX ("03" Hex)
	memcpy(&szEcrBuffer[inReqPacketIndex], ETX, ETX_SIZE);
	inReqPacketIndex++;

	//LRC
	xorOpBtwnChars ((unsigned char *)szEcrBuffer, inReqPacketIndex, &inLCR);
	memset(szLCR, 0x00, sizeof(szLCR));
	memset(szLCR_Hex, 0x00, sizeof(szLCR_Hex));
	sprintf(szLCR, "%02x", inLCR);
	if(strlen(szLCR) == 1)
		ascToHexConv(szLCR_Hex, szLCR, 1);
	else
		ascToHexConv(szLCR_Hex, szLCR, 2);
	memcpy(&szEcrBuffer[inReqPacketIndex], szLCR_Hex, LCR_SIZE);	//Excusive OR of each character of message including STX and ETX.
    szEcrBuffer[inReqPacketIndex+1] = '\0';
}

EXPORT void parse(char *respData, char *respOutData)
{
	int inRespDataIndex = 0;
	char szDelimiter[DELIMITER_SIZE], szRespDataChar_hex[DELIMITER_SIZE];

	memset(szDelimiter, 0x00, sizeof(szDelimiter));
	memset(szRespDataChar_hex, 0x00, sizeof(szRespDataChar_hex));
	memcpy(szDelimiter, OUT_DELIMITER, strlen(OUT_DELIMITER));
	for(inRespDataIndex=0; inRespDataIndex<strlen(respData); inRespDataIndex++)
	{
		sprintf(szRespDataChar_hex, "%x", respData[inRespDataIndex]);
		if(!strcmp(szRespDataChar_hex, szDelimiter))
		{
			respOutData[inRespDataIndex] = DELIMITOR_CHAR;
		}
		if(strcmp(szRespDataChar_hex, szDelimiter))
		{
			memcpy(&respOutData[inRespDataIndex], &respData[inRespDataIndex], 1);
		}
	}
}

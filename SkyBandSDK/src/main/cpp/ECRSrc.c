/*
 * ECRSrc.c
 *
 *  Created on: 05-Mar-2020
 *      Author: kopresh
 */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "ECRSrc.h"
#include "Utilities.h"

extern void hexDataPrint(char * headerString, unsigned char * inputBuffer, int numBytes);
extern void ascToHexConv (unsigned char *outp, unsigned char *inp, int iLength);
extern void xorOpBtwnChars (unsigned char *pbt_Data1, int i_DataLen, int *output);

void vdParseRequestData(char *inputReqData, long long reqFields[], int *count)
{
	char szItem[100];
	int inReqDataIndex = 0, inItemsCount = 0, inFieldIndex = 0;

	memset(szItem, 0x00, sizeof(szItem));
	for(inReqDataIndex=0; inReqDataIndex<strlen(inputReqData); inReqDataIndex++)
	{
		if(inputReqData[inReqDataIndex] == DELIMITOR_CHAR)
		{
			reqFields[inItemsCount] = atoll(szItem);
			inItemsCount++;
			memset(szItem, 0x00, sizeof(szItem));
			inFieldIndex = 0;
		}
		if(inputReqData[inReqDataIndex] != DELIMITOR_CHAR)
		{
			szItem[inFieldIndex] = inputReqData[inReqDataIndex];
			inFieldIndex++;
			if(inputReqData[inReqDataIndex] == ENDMSG_CHAR)
			{
				reqFields[inItemsCount] = atoll(szItem);
				inItemsCount++;
			}
		}
	}
	*count = inItemsCount;
}

char *getCommand(int tranType)
{
	//printf("Get Command\n");
	switch(tranType)
	{
		case TYPE_PURCHASE:
			return CMD_PURCHASE;	//Purchase

		case TYPE_PURCHASE_CASHBACK:
			return CMD_PURCHASE_CASHBACK; 	//Purchase cashback

		case TYPE_REFUND:
			return CMD_REFUND;		// Refund

		case TYPE_PREAUTH:
			return CMD_PREAUTH;		//Pre-authorization

		case TYPE_REVERSAL:
			return CMD_REVERSAL;		//Reversal

		case TYPE_PRECOMP:
			return CMD_PRECOMP;		//Pre-Auth Completion

		case TYPE_PREAUTH_EXT:
			return CMD_PREAUTH_EXT; 	//Pre-Auth Extension

		case TYPE_PREAUTH_VOID:
			return CMD_PREAUTH_VOID; 	//Pre-Auth VOID

		case TYPE_CASH_ADVANCE:
			return CMD_CASH_ADVANCE;	//Cash Advance

		case TYPE_RECONCILATION:
			return CMD_SETTLEMENT;	//Settlement

		case TYPE_PARAM_DOWNLOAD:
			return CMD_PARAM_DOWNLOAD;	//Parameter Download

		case TYPE_SET_PARAM:
			return CMD_SET_PARAM;	//Get Parameter

		case TYPE_GET_PARAM:
			return CMD_GET_PARAM;	//Get Parameter

		case TYPE_SET_TERM_LANG:
			return CMD_SET_TERM_LANG;	//Set Terminal Language

		case TYPE_REGISTER:
			return CMD_REGISTER;	//Register

		case TYPE_START_SESSION:
			return CMD_START_SESSION;	//Start Session

		case TYPE_END_SESSION:
			return CMD_END_SESSION;		//End Session

		case TYPE_BILL_PAY:
			return CMD_BILL_PAY;		//Bill Pay

		case TYPE_PRNT_DETAIL_RPORT:
			return CMD_PRNT_DETAIL_RPORT;	//Print detail report

		case TYPE_PRNT_SUMMARY_RPORT:
			return CMD_PRNT_SUMMARY_RPORT;	//Print summary report

		case TYPE_REPEAT:
			return CMD_REPEAT;	//Repeat

		case TYPE_CHECK_STATUS:
			return CMD_CHECK_STATUS;	//Check Status

		default:
			break;
	}
}

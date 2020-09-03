Insert into OFFENDER (OFFENDER_ID,FIRST_NAME,CRN,SECOND_NAME,PNC_NUMBER,THIRD_NAME,CRO_NUMBER,SURNAME,NOMS_NUMBER,PREVIOUS_SURNAME,ALLOW_SMS,DATE_OF_BIRTH_DATE,NI_NUMBER,NOTES,LANGUAGE_CONCERNS,DECEASED_DATE,INTERPRETER_REQUIRED,IMMIGRATION_NUMBER,EXCLUSION_MESSAGE,RESTRICTION_MESSAGE,TELEPHONE_NUMBER,MOBILE_NUMBER,E_MAIL_ADDRESS,PARTITION_AREA_ID,SOFT_DELETED,ROW_VERSION,TITLE_ID,GENDER_ID,ETHNICITY_ID,NATIONALITY_ID,IMMIGRATION_STATUS_ID,LANGUAGE_ID,RELIGION_ID,SURNAME_SOUNDEX,CREATED_DATETIME,MOST_RECENT_PRISONER_NUMBER,LAST_UPDATED_DATETIME,LAST_UPDATED_DATETIME_DIVERSIT,SECOND_NATIONALITY_ID,SEXUAL_ORIENTATION_ID,CURRENT_EXCLUSION,CREATED_BY_USER_ID,LAST_UPDATED_USER_ID,TRAINING_SESSION_ID,FIRST_NAME_SOUNDEX,MIDDLE_NAME_SOUNDEX,LAST_UPDATED_USER_ID_DIVERSITY,CURRENT_DISPOSAL,CURRENT_HIGHEST_RISK_COLOUR,CURRENT_RESTRICTION,INSTITUTION_ID,ESTABLISHMENT,ORGANISATIONS,CURRENT_TIER,PENDING_TRANSFER,OFFENDER_DETAILS,PREVIOUS_CONVICTION_DATE,PREVIOUS_CONVICTION_DOCUMENT,PREV_CONVICTION_DOCUMENT_NAME,CURRENT_REMAND_STATUS,OM_ALLOCATION_DECISION_ID,PREV_CON_LAST_UPDATED_USER_ID,PREV_CON_LAST_UPD_AUTH_PROV_ID,PREV_CON_CREATED_PROVIDER_ID,PREV_CON_CREATED_BY_USER_ID,PREV_CON_CREATED_DATETIME,PREV_CON_ALFRESCO_DOCUMENT_ID,TRANSGENDER_CONSENT_DISCLOSE,TRANSGENDER_PROCESS_ID,IOM_NOMINAL,SAFEGUARDING_ISSUE,VULNERABILITY_ISSUE,DISABILITY,LEARNING,MENTAL_HEALTH,CDC_CUSTODY_START_DATE,DYNAMIC_RSR_SCORE) values (2600343964,'Aadland','X320811',null,null,null,null,'Bertrand','G9642VP',null,null,to_date('19-JUL-65','DD-MON-RR'),null, null,null,null,null,null,'You are excluded from viewing this offender record. Please contact a system administrator','This is a restricted offender record. Please contact a system administrator',null,null,null,0,0,15,null,545,null,null,null,null,null,'B636',to_date('04-SEP-19','DD-MON-RR'),'V74112',to_date('27-SEP-19','DD-MON-RR'),to_date('04-SEP-19','DD-MON-RR'),null,null,0,2500040507,2500040507,null,'A345',null,2500040507,1,null,0,2500004521,'Y',('2600007020', '1500001001'),2600031092,0, null,to_date('01-SEP-19','DD-MON-RR'), null,'PRE-CONS.pdf','Bail - Unconditional',2600005001,2500040507,1500001001,1500001001,2500040507,to_date('10-SEP-19','DD-MON-RR'),'1e593ff6-d5d6-4048-a671-cdeb8f651234',null,null,null,null,null,null,null,null,to_date('04-SEP-19','DD-MON-RR'),1);
Insert into CUSTODY (CUSTODY_ID,DISPOSAL_ID,STATUS_CHANGE_DATE,PRISONER_NUMBER,PAROLE_NUMBER,LOCATION_CHANGE_DATE,SOFT_DELETED,PARTITION_AREA_ID,ROW_VERSION,CUSTODIAL_STATUS_ID,PC_TELEPHONE_NUMBER,PRISON_OFFICER,PROBATION_CONTACT,PO_TELEPHONE_NUMBER,CREATED_DATETIME,LAST_UPDATED_DATETIME,INSTITUTION_ID,CREATED_BY_USER_ID,ESTABLISHMENT,LAST_UPDATED_USER_ID,TRAINING_SESSION_ID,OFFENDER_ID,ORGANISATIONS,PSS_START_DATE) values (2600151980,2600282123,to_date('04-SEP-19','DD-MON-RR'),'V74112',null,to_date('04-SEP-19','DD-MON-RR'),0,0,3,2500000632,null,null,null,null,to_date('04-SEP-19','DD-MON-RR'),to_date('27-SEP-19','DD-MON-RR'),2500004521,2500040507,'Y',2500040507,null,2600343964,('2600007020', '1500001001'),to_date('03-NOV-19', 'DD-MON-RR'));
Insert into DISPOSAL (DISPOSAL_ID,DISPOSAL_DATE,VALUE,LENGTH,PUNISHMENT,REDUCTION_OF_CRIME,REFORM_AND_REHABILITION,PUBLIC_PROTECTION,REPARATION,RECOMMENDATION_NOT_STATED,TERMINATION_DATE,TERMINATION_NOTES,EVENT_ID,PARTITION_AREA_ID,SOFT_DELETED,ROW_VERSION,LEVEL_OF_SERIOUSNESS_ID,DISPOSAL_TYPE_ID,CREATED_DATETIME,CREATED_BY_USER_ID,LAST_UPDATED_DATETIME,DISPOSAL_TERMINATION_REASON_ID,LAST_UPDATED_USER_ID,TRAINING_SESSION_ID,LENGTH_2,OFFENDER_ID,ORGANISATIONS,ACTIVE_FLAG,UPW,EFFECTIVE_LENGTH,LENGTH_IN_DAYS,ENTRY_LENGTH_UNITS_ID,ENTRY_LENGTH_2_UNITS_ID,NOTIONAL_END_DATE,ENTRY_LENGTH,USER_TERMINATION_DATE,ENTERED_NOTIONAL_END_DATE) values (2600282123,to_date('03-DEC-18','DD-MON-RR'),null,11,'N','N','N','N','N','N',null, null,2600295124,0,0,1,516,41,to_date('04-SEP-19','DD-MON-RR'),2500040507,to_date('04-SEP-19','DD-MON-RR'),null,2500040507,null,5,2600343964,('2600007020', '1500001001'),1,0,null,1826,1111,1111,to_date('03-SEP-24','DD-MON-RR'),5,null,null);
Insert into EVENT (EVENT_ID,CONSECUTIVE_TO_EVENT_ID,CONCURRENT_WITH_EVENT_ID,OFFENDER_ID,EVENT_NUMBER,REFERRAL_DATE,NOTES,SOFT_DELETED,PARTITION_AREA_ID,ROW_VERSION,CREATED_BY_USER_ID,CREATED_DATETIME,LAST_UPDATED_USER_ID,LAST_UPDATED_DATETIME,TRAINING_SESSION_ID,ORGANISATIONS,IN_BREACH,ACTIVE_FLAG,BREACH_END,CPS_DATE,CPS_DOCUMENT,CPS_DOCUMENT_NAME,FTC_COUNT,PENDING_TRANSFER,CONVICTION_DATE,FIRST_RELEASE_DATE,PSS_RQMNT_FLAG,CPS_LAST_UPDATED_USER_ID,CPS_LAST_UPDATED_AUTH_PROV_ID,CPS_CREATED_PROVIDER_ID,CPS_CREATED_BY_USER_ID,CPS_CREATED_DATETIME,CPS_ALFRESCO_DOCUMENT_ID,CPS_SOFT_DELETED,COURT_ID) values (2600295124,null,null,2600343964,'4',to_date('04-SEP-18','DD-MON-RR'), null,0,0,4,2500040507,to_date('04-SEP-19','DD-MON-RR'),2500040507,to_date('04-SEP-19','DD-MON-RR'),null,('2600007020', '1500001001'),0,1,null,to_date('02-SEP-19','DD-MON-RR'), null,'CPSPack-1.txt',0,0,to_date('03-SEP-19','DD-MON-RR'),null,0,2500040507,1500001001,1500001001,2500040507,to_date('04-SEP-19','DD-MON-RR'),'12345',0,1500004905);
Insert into MAIN_OFFENCE (MAIN_OFFENCE_ID,OFFENCE_DATE,OFFENCE_COUNT,EVENT_ID,TICS,VERDICT,SOFT_DELETED,PARTITION_AREA_ID,ROW_VERSION,OFFENCE_ID,TRAINING_SESSION_ID,OFFENDER_ID,ORGANISATIONS,CREATED_BY_USER_ID,CREATED_DATETIME,LAST_UPDATED_USER_ID,LAST_UPDATED_DATETIME) values (2600297062,to_date('09-SEP-19','DD-MON-RR'),1,2600295124,null,null,0,0,1,1107,null,2600343964,('2600007020', '1500001001'),2500040507,to_date('17-SEP-19','DD-MON-RR'),2500040507,to_date('17-SEP-19','DD-MON-RR'));
Insert into RELEASE (RELEASE_ID,ACTUAL_RELEASE_DATE,NOTES,PARTITION_AREA_ID,SOFT_DELETED,ROW_VERSION,RELEASE_TYPE_ID,CUSTODY_ID,CREATED_BY_USER_ID,LAST_UPDATED_USER_ID,CREATED_DATETIME,LAST_UPDATED_DATETIME,TRAINING_SESSION_ID,OFFENDER_ID,ORGANISATIONS,LENGTH,INSTITUTION_ID,ESTABLISHMENT,PROBATION_AREA_ID) values (2600079183,to_date('03-JUL-19','DD-MON-RR'), 'Release notes',0,0,0,1041,2600151980,2500040507,2500040507,to_date('11-OCT-19','DD-MON-RR'),to_date('11-OCT-19','DD-MON-RR'),null,2600343964,('2600007020', '1500001001'),null,2500004521,'Y',null);
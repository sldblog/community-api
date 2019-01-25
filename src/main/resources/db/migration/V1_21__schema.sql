create table DISABILITY
(
  DISABILITY_ID NUMBER not null
    constraint XPKDISABILITY
      primary key,
  OFFENDER_ID NUMBER not null
    references OFFENDER,
  START_DATE DATE not null,
  FINISH_DATE DATE,
  NOTES CLOB,
  SOFT_DELETED NUMBER not null,
  PARTITION_AREA_ID NUMBER not null
    references PARTITION_AREA,
  ROW_VERSION NUMBER default 0 not null,
  DISABILITY_TYPE_ID NUMBER not null
    references R_STANDARD_REFERENCE_LIST,
  TRAINING_SESSION_ID NUMBER,
  ORGANISATIONS T_ARR_ORGANISATIONS,
  CREATED_BY_USER_ID NUMBER not null,
  LAST_UPDATED_USER_ID NUMBER not null,
  CREATED_DATETIME DATE not null,
  LAST_UPDATED_DATETIME DATE not null,
  constraint XAK1DISABILITY
    unique (OFFENDER_ID, DISABILITY_TYPE_ID, FINISH_DATE, DISABILITY_ID)
);

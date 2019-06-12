
DROP TABLE [dbo].[Report];

DROP TABLE [dbo].[ReportVariable];


CREATE TABLE [dbo].[Report]
(
  [ReportID] [numeric](19, 0) IDENTITY(1,1) NOT NULL,
  [SubscriberID] [numeric](19, 0),
  [IntegrationProcessExecutionID] [numeric](19, 0),
  [ReportType] [varchar](255) NOT NULL,
  [ReportDate] [datetime] ,
  PRIMARY KEY CLUSTERED 
(
	[ReportID] ASC
) WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]; 


CREATE TABLE [dbo].[ReportVariable]
(
  [ReportVariableID] [numeric](19, 0) IDENTITY(1,1) NOT NULL,
  [ReportID] [numeric](19, 0) NOT NULL,
  [RowNumber] [numeric](19, 0) NOT NULL,
  [ReportVariableName] [varchar](255) NOT NULL,
  [ReportVariableValue] [varchar](255) NOT NULL,
  PRIMARY KEY CLUSTERED 
(
	[ReportVariableID] ASC
) WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]; 
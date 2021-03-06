--
-- Copyright 2005-2014 The Kuali Foundation
--
-- Licensed under the Educational Community License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
--
-- http://www.opensource.org/licenses/ecl2.php
--
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
--

TRUNCATE TABLE KRMS_ACTN_T DROP STORAGE
/
INSERT INTO KRMS_ACTN_T (ACTN_ID,DESC_TXT,NM,NMSPC_CD,RULE_ID,SEQ_NO,TYP_ID,VER_NBR)
  VALUES ('T1000','Action Stub for Testing','testAction','KR-RULE-TEST','T1000',1,'T1002',1)
/

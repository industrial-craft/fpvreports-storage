DELETE FROM fpv_report;
DELETE FROM fpv_pilot;
ALTER TABLE fpv_report ALTER COLUMN fpv_report_id RESTART WITH 1;
ALTER TABLE fpv_pilot ALTER COLUMN fpv_pilot_id RESTART WITH 1;
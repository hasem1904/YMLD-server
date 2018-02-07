--
-- Export data from YMLD
--
CALL SYSCS_UTIL.SYSCS_EXPORT_TABLE ('APP','SEATS','/home/havard/Documents/Repo/Local/YMLD-Backend/db/export/seats.dat',null,null,null);
CALL SYSCS_UTIL.SYSCS_EXPORT_TABLE ('APP','SEATMAP','/home/havard/Documents/Repo/Local/YMLD-Backend/db/export/seatmap.dat',null,null,null);
CALL SYSCS_UTIL.SYSCS_EXPORT_TABLE ('APP','PERFORMANCES','/home/havard/Documents/Repo/Local/YMLD-Backend/db/export/performances.dat',null,null,null);
CALL SYSCS_UTIL.SYSCS_EXPORT_TABLE ('APP','PRODUCTIONS','/home/havard/Documents/Repo/Local/YMLD-Backend/db/export/productions.dat',null,null,null);
CALL SYSCS_UTIL.SYSCS_EXPORT_TABLE ('APP','TRANSACTIONS','/home/havard/Documents/Repo/Local/YMLD-Backend/db/export/transactions.dat',null,null,null);
#!/bin/bash

user=root
passwd=mob_root
database=azkaban_offline
azkaban_user=azkaban_offline
azkaban_passwd=azkaban_offline


MYSQL="mysql -u$user -p$passwd"

CRETAE_DB="CREATE DATABASE ${database};"
CREATE_USER="CREATE USER '${azkaban_user}'@'%' IDENTIFIED BY '${azkaban_passwd}';"
GRAND_USER_REMO="GRANT SELECT,INSERT,UPDATE,DELETE,INDEX ON ${database}.* to '${azkaban_user}'@'%' WITH GRANT OPTION;"
GRAND_USER_LOCAL="GRANT SELECT,INSERT,UPDATE,DELETE,INDEX ON ${database}.* to '${azkaban_user}'@'localhost' WITH GRANT OPTION;"


$MYSQL -e ${CRETAE_DB}
$MYSQL -e ${CREATE_USER}
$MYSQL -e ${GRAND_USER_REMO}
$MYSQL -e ${GRAND_USER_LOCAL}



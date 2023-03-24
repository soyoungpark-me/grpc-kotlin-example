DROP TABLE IF EXISTS CATALOG;
CREATE TABLE CATALOG
(
    id                  int(10)              NOT NULL AUTO_INCREMENT,
    name                varchar(100)         NOT NULL,
    content             text,
    update_date         timestamp            NULL,
    update_id           varchar(10)          NULL,
    PRIMARY KEY (id)
);
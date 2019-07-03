CREATE DATABASE bc;
use bc;
CREATE TABLE `user_table` (    
 `username` VARCHAR(100) NOT NULL ,    
 `password` VARCHAR(100) NOT NULL ,    
 `uid` INT NOT NULL AUTO_INCREMENT,
 PRIMARY KEY  (`uid`)  
)DEFAULT CHARSET=utf8;
ALTER TABLE user_table ADD UNIQUE (username);  

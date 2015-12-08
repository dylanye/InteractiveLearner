CREATE TABLE words(
  word string,
  docs integer,
  PRIMARY KEY (word)
);

CREATE TABLE positive_words (
  posword string NOT NULL ,
  count integer,
  posprob integer,
  PRIMARY KEY (posword),
  FOREIGN KEY (posword) REFERENCES words(word)
);

CREATE TABLE negative_words (
  negword string NOT NULL,
  count integer,
  negprob integer,
  PRIMARY KEY (negword),
  FOREIGN KEY (negword) REFERENCES words(word)
);

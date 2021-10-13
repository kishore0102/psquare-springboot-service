select userid_fntest(2500000001);
CREATE OR REPLACE FUNCTION userid_fntest (test bigint)
RETURNS text AS $useridc$
declare
	userid1 integer;
	userid2 integer;
	userid3 integer;
	userid4 integer;
	userid5 integer;
	userid6 integer;
	userid7 integer;
	userid8 integer;
	userid9 integer;
	userid0 integer;
	nextvalue bigint;
	useridc text;
BEGIN
   nextvalue = test;
   userid1 = nextvalue / 2600000000 % 26 + 65;
   userid2 = nextvalue / 260000000 % 26 + 65;
   userid3 = nextvalue / 10000000 % 26 + 65;
   userid4 = nextvalue / 1000000 % 10 + 48;
   userid5 = nextvalue / 100000 % 10 + 48;
   userid6 = nextvalue / 10000 % 10 + 48;
   userid7 = nextvalue / 1000 % 10 + 48;
   userid8 = nextvalue / 100 % 10 + 48;
   userid9 = nextvalue / 10 % 10 + 48;
   userid0 = nextvalue % 10 + 48;
   useridc = chr(userid1) || chr(userid2) || chr(userid3) ||
   			 chr(userid4) || chr(userid5) || chr(userid6)  ||
			 chr(userid7) || chr(userid8) || chr(userid9) ||
   			 chr(userid0);
   RETURN useridc;
END;
$useridc$ LANGUAGE plpgsql;

------------------------------------------------------------------------------

 CREATE SEQUENCE userid_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1

CREATE OR REPLACE FUNCTION userid_fn ()
RETURNS text AS $useridc$
declare
	userid1 integer;
	userid2 integer;
	userid3 integer;
	userid4 integer;
	userid5 integer;
	userid6 integer;
	userid7 integer;
	userid8 integer;
	userid9 integer;
	userid0 integer;
	nextvalue bigint;
	useridc text;
BEGIN
   nextvalue = nextval('userid_seq');
   userid1 = nextvalue / 2600000000 % 26 + 65;
   userid2 = nextvalue / 260000000 % 26 + 65;
   userid3 = nextvalue / 10000000 % 26 + 65;
   userid4 = nextvalue / 1000000 % 10 + 48;
   userid5 = nextvalue / 100000 % 10 + 48;
   userid6 = nextvalue / 10000 % 10 + 48;
   userid7 = nextvalue / 1000 % 10 + 48;
   userid8 = nextvalue / 100 % 10 + 48;
   userid9 = nextvalue / 10 % 10 + 48;
   userid0 = nextvalue % 10 + 48;
   useridc = chr(userid1) || chr(userid2) || chr(userid3) ||
   			 chr(userid4) || chr(userid5) || chr(userid6)  ||
			 chr(userid7) || chr(userid8) || chr(userid9) ||
   			 chr(userid0);
   RETURN useridc;
END;
$useridc$ LANGUAGE plpgsql;

select userid_fn();
select nextval('userid_seq');

------------------------------------------------------------------------------

CREATE TABLE user_details (
	userid VARCHAR(10) PRIMARY KEY DEFAULT userid_fn(),
	email VARCHAR(255) UNIQUE NOT NULL,
	firstname VARCHAR(50) NOT NULL,
	lastname VARCHAR(50) NOT NULL,
	status char,
	passhash VARCHAR(255) NOT NULL
);

ALTER TABLE table_name 
DROP COLUMN column_name;


CREATE TABLE user_details
(
    userid character varying(10) NOT NULL DEFAULT userid_fn(),
    email character varying(255) NOT NULL,
    firstname character varying(50) NOT NULL,
    lastname character varying(50) NOT NULL,
    status character(1),
    passhash character varying(255) NOT NULL,
    lockcount integer DEFAULT 0,
    otp character varying(6),
    otpts timestamp with time zone,
    otpvalidator integer DEFAULT 0,
    CONSTRAINT user_details_pkey PRIMARY KEY (userid),
    CONSTRAINT user_details_email_key UNIQUE (email)
)

------------------------------------------------------------------------------

CREATE TABLE notes_data
(
    userid character varying(10) NOT NULL,
    seqnbr bigint NOT NULL,
    topic text,
    description text,
    status "char",
	createdat TIMESTAMP WITH TIME ZONE DEFAULT current_timestamp,
	updatedat TIMESTAMP WITH TIME ZONE DEFAULT current_timestamp,
    CONSTRAINT notes_data_pkey PRIMARY KEY (userid, seqnbr)
);

CREATE OR REPLACE FUNCTION updatedts_function() 
RETURNS TRIGGER AS $$
BEGIN
    NEW.updatedat = now();
    RETURN NEW; 
END;
$$ language 'plpgsql';

CREATE TRIGGER updatedts_trigger
BEFORE UPDATE ON notes_data FOR EACH ROW 
EXECUTE PROCEDURE updatedts_function();




--------------------------------------------------------------------


CREATE TABLE calendar_data(
    userid character varying(10) NOT NULL,
    seqnbr bigint NOT NULL,
    eventoccurs character varying NOT NULL,
	title text NOT NULL,
    description text,
    starttime timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    endtime timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    reminder timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT calendar_data_pkey PRIMARY KEY (userid, seqnbr)
)

















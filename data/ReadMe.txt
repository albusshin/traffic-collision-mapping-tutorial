1. File
	ReadMe.txt - This instruction file.
	mapping.txt - A mapping between the excel reports and the table.
	schema.sql - The schema for the tables.
	sample_data - A folder including the sql sample data file  for the table.

2. In your VM, create a database in Vertica and connect it in your terminal.(Name the database as you like)

3. Use below command to create the table schema. (Assume you have uploaded my submission into your VM)
   {YOURDBNAME}=>\i {path_to_my_submission_root_folder}/schema.sql

4. Use below command to insert data into nypd_motor_vehicle_collisions table.
   {YOURDBNAME}=>\i {path_to_my_submission_root_folder}/sample_data/data_nypd_motor_vehicle_collisions.sql
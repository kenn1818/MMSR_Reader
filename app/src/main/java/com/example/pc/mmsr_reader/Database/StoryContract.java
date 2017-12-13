package com.example.pc.mmsr_reader.Database;

import android.provider.BaseColumns;

public final class StoryContract {
	public StoryContract(){}
	
	public static abstract class MMSRLibrary implements BaseColumns {
		public static final String TABLE_NAME ="library";
		public static final String COLUMN_STORYBOOKID = "storybookID";
		public static final String COLUMN_TITLE ="title";
		public static final String COLUMN_DESCRIPTION ="desc";
		public static final String COLUMN_LANGUAGE = "language";
		public static final String COLUMN_AGEGROUP ="ageGroup";
		public static final String COLUMN_EMAIL ="email";
		public static final String COLUMN_DATEOFCREATION = "dateOfCreation";
		public static final String COLUMN_COVERPAGE ="coverPage";
		public static final String COLUMN_RATE ="rate";
	}
}

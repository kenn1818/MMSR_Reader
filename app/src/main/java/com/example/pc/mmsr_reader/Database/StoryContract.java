package com.example.pc.mmsr_reader.Database;

import android.provider.BaseColumns;
//TODO: modify this class to match with the Storybook
public final class StoryContract {
	public StoryContract(){}
	
	public static abstract class MMSRLibrary implements BaseColumns {
		public static final String TABLE_NAME ="library";
		public static final String COLUMN_PHONE = "phone";
		public static final String COLUMN_NAME ="name";
		public static final String COLUMN_EMAIL ="email";
	}
}

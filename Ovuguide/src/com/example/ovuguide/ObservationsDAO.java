package com.example.ovuguide;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class ObservationsDAO {
	
	
	private SQLiteDatabase observations;
	private ObservationsSQLiteHelper observationsSQLiteHelper;
	
	public ObservationsDAO(Context c) {
		observationsSQLiteHelper = new ObservationsSQLiteHelper(c);
	}
	
	public void open()
	{
		observations = observationsSQLiteHelper.getWritableDatabase();
	}
	
	public void close()
	{
		observationsSQLiteHelper.close();
	}
	
	public boolean addDailyReadings(DailyReading dailyReading)
	{
		ContentValues contentValues = new ContentValues();
		contentValues.put(ObservationsSQLiteHelper.DAY_OF_MONTH, dailyReading.getDayOfMonth());
		contentValues.put(ObservationsSQLiteHelper.MONTH, dailyReading.getMonth());
		contentValues.put(ObservationsSQLiteHelper.YEAR, dailyReading.getYear());
		contentValues.put(ObservationsSQLiteHelper.MUCUS, dailyReading.getMucus());
		//contentValues.put(ObservationsSQLiteHelper.TEMPERATURE, dailyReading.getTemperature());
		contentValues.put(ObservationsSQLiteHelper.PHASE, dailyReading.getPhase());
		
		open();
		long result = observations.insert(ObservationsSQLiteHelper.TABLE_NAME, null, contentValues);
		close();
		
		if(result!=-1)
		{
			DailyReading newReading = dailyReading;			
			return updateDailyReading(newReading);
		}
		else
			return false;
		
	}
	
	public boolean updateDailyReading(DailyReading dailyReading)
	{
		open();
		ContentValues contentValues = new ContentValues();
		contentValues.put(ObservationsSQLiteHelper.DAY_OF_MONTH, dailyReading.getDayOfMonth());
		contentValues.put(ObservationsSQLiteHelper.MONTH, dailyReading.getMonth());
		contentValues.put(ObservationsSQLiteHelper.YEAR, dailyReading.getYear());
		contentValues.put(ObservationsSQLiteHelper.MUCUS, dailyReading.getPhase());
		//contentValues.put(ObservationsSQLiteHelper.TEMPERATURE, dailyReading.getTemperature());
		contentValues.put(ObservationsSQLiteHelper.PHASE, dailyReading.getPhase());
		
		String whereClause = ObservationsSQLiteHelper.DAY_OF_MONTH+"=? and "+ObservationsSQLiteHelper.MONTH+"=? and "+ObservationsSQLiteHelper.YEAR+"=?";
		String whereArgs[]={(dailyReading.getDayOfMonth()+""),(dailyReading.getMonth()+""),(dailyReading.getYear()+"")};
		
		int result = observations.update(ObservationsSQLiteHelper.TABLE_NAME,contentValues,whereClause,whereArgs);
		close();
		if(result!=0)
			return true;
		return false;
	}
	
	
	public DailyReading getDailyReading(int dayOfMonth, int month,int year)
	{
		open();
		DailyReading dailyReading = new DailyReading();
		
		String allColumns[] = {ObservationsSQLiteHelper.DAY_OF_MONTH,ObservationsSQLiteHelper.MONTH,ObservationsSQLiteHelper.YEAR,ObservationsSQLiteHelper.MUCUS,ObservationsSQLiteHelper.TEMPERATURE,ObservationsSQLiteHelper.PHASE};
		String whereClause = ObservationsSQLiteHelper.DAY_OF_MONTH+"=? and "+ObservationsSQLiteHelper.MONTH+"=? and "+ObservationsSQLiteHelper.YEAR+"=?";
		String whereArgs[]={(dayOfMonth+""),(month+""),(year+"")};
		
		Cursor cursor = observations.query(ObservationsSQLiteHelper.TABLE_NAME, null, whereClause, whereArgs, null, null, null);				
		
				
		cursor.moveToFirst();
		if(cursor==null)
			return null;
		dailyReading.setDayOfMonth(cursor.getInt(0));
		dailyReading.setMonth(cursor.getInt(1));
		dailyReading.setYear(cursor.getInt(2));
		dailyReading.setMucus(cursor.getInt(3));
		dailyReading.setPhase(cursor.getInt(4));
		close();		
		
		return dailyReading;
	
	}
	
	/*public void addDailyPhase(int dayOfMonth, int month,int year,int phase)
	{
		ContentValues contentValues = new ContentValues();
		contentValues.put(ObservationsSQLiteHelper.PHASE, phase);
		String whereClause = ObservationsSQLiteHelper.DAY_OF_MONTH+"=? and "+ObservationsSQLiteHelper.MONTH+"=? and "+ObservationsSQLiteHelper.YEAR+"=?";
		String whereArgs[]={dayOfMonth+"",month+"",year+""};
		observations.update(ObservationsSQLiteHelper.TABLE_NAME, contentValues,whereClause , whereArgs);
	}*/
}

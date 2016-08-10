package com.coolweather.app.db;

import java.util.ArrayList;
import java.util.List;

import com.coolweather.app.model.City;
import com.coolweather.app.model.Country;
import com.coolweather.app.model.Province;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.UrlQuerySanitizer.ValueSanitizer;

public class CoolWeatherDB
{
	public static final String DB_NAME = "cool_weather";
	public static final int VERSION = 1;
	private static CoolWeatherDB coolWeatherDB;
	
	private SQLiteDatabase db;
	
	public CoolWeatherDB(Context context)
	{
		CoolWeatherOpenHelper dbHelper = new CoolWeatherOpenHelper(context,DB_NAME,null,VERSION);
		db = dbHelper.getWritableDatabase();
	}
	
	public synchronized static CoolWeatherDB getInstance(Context context)
	{
		if(coolWeatherDB == null)
		{
			coolWeatherDB = new CoolWeatherDB(context);
		}
		return coolWeatherDB;
	}
	
	public void saveProvince(Province province)
	{
		if(province!=null)
		{
			ContentValues values = new ContentValues();
			values.put("province_name",province.getProvinceName());
			values.put("province_code",province.getProvinceCode());
			db.insert("Province",null,values);
		}
	}
	
	public List<Province> loadProvince()
	{
		List<Province> list = new ArrayList<Province>();
		Cursor cursor = db.query("Province",null,null,null,null,null,null);
		if(cursor.moveToFirst())
		{
			do
			{
				Province province = new Province();
				province.setId(cursor.getInt(cursor.getColumnIndex("id")));
				province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
				province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
				list.add(province);
				
			} while (cursor.moveToNext());
		}
		return list;
	}
	
	public void saveCity(City city)
	{
		if(city!=null)
		{
			ContentValues contentValues = new ContentValues();
			contentValues.put("city_name",city.getCityName());
			contentValues.put("city_code",city.getCityCode());
			contentValues.put("province_id",city.getProvinceId());
			db.insert("City",null,contentValues);
		}
	}
	
	public List<City> loadCities(int provinceId)
	{
		List<City> list = new ArrayList<City>();
		Cursor cursor = db.query("City",null,"province_id=?",new String[]{String.valueOf(provinceId)},null,null,null);
		if(cursor.moveToFirst())
		{
			City city = new City();
			city.setId(cursor.getInt(cursor.getColumnIndex("id")));
			city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
			city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
			city.setProvinceId(provinceId);
			list.add(city);
		}
		return list;
	}
	
	
	public void saveCounty(Country country) 
	{
		if (country != null) 
		{
			ContentValues values = new ContentValues();
			values.put("county_name", country.getCountryName());
			values.put("county_code", country.getCountryCode());
			values.put("city_id", country.getCityId());
			db.insert("County", null, values);
		}
	}
	
	
	public List<Country> loadCounties(int cityId) 
	{
		List<Country> list = new ArrayList<Country>();
		Cursor cursor = db.query("County", null, "city_id = ?",
		new String[] { String.valueOf(cityId) }, null, null, null);
		if (cursor.moveToFirst()) 
		{
			do 
			{
				Country county = new Country();
				county.setId(cursor.getInt(cursor.getColumnIndex("id")));
				county.setCountryName(cursor.getString(cursor.getColumnIndex("county_name")));
				county.setCountryCode(cursor.getString(cursor.getColumnIndex("county_code")));
				county.setCityId(cityId);
				list.add(county);
			} 
			while (cursor.moveToNext());
		}
		return list;
	}
}

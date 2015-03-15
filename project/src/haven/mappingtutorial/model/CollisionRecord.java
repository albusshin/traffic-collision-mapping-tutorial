package haven.mappingtutorial.model;

import java.sql.Time;
import java.util.Date;

/**
 * A CollisionRecord Model class
 */
public class CollisionRecord {
	private Date date;
	private Time time;
	private String borough;
	private String zipCode;
	private double latitude;
	private double longitude;
	private String location;
	private String onStreetName;
	private String crossStreetName;
	private String offStreetName;
	private int injuredPersons;
	private int killedPersons;
	private int injuredPedestrians;
	private int killedPedestrians;
	private int injuredCyclist;
	private int killedCyclist;
	private int injuredMotorist;
	private int killedMotorist;
	private String contributingFactorVehicle1;
	private String contributingFactorVehicle2;
	private String contributingFactorVehicle3;
	private String contributingFactorVehicle4;
	private String contributingFactorVehicle5;
	private long uniqueKey;
	private String vehicleTypeCode1;
	private String vehicleTypeCode2;
	private String vehicleTypeCode3;
	private String vehicleTypeCode4;
	private String vehicleTypeCode5;
	
	/* Generated Getters and Setters */
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Time getTime() {
		return time;
	}
	public void setTime(Time time) {
		this.time = time;
	}
	public String getBorough() {
		return borough;
	}
	public void setBorough(String borough) {
		this.borough = borough;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getOnStreetName() {
		return onStreetName;
	}
	public void setOnStreetName(String onStreetName) {
		this.onStreetName = onStreetName;
	}
	public String getCrossStreetName() {
		return crossStreetName;
	}
	public void setCrossStreetName(String crossStreetName) {
		this.crossStreetName = crossStreetName;
	}
	public String getOffStreetName() {
		return offStreetName;
	}
	public void setOffStreetName(String offStreetName) {
		this.offStreetName = offStreetName;
	}
	public int getInjuredPersons() {
		return injuredPersons;
	}
	public void setInjuredPersons(int injuredPersons) {
		this.injuredPersons = injuredPersons;
	}
	public int getKilledPersons() {
		return killedPersons;
	}
	public void setKilledPersons(int killedPersons) {
		this.killedPersons = killedPersons;
	}
	public int getInjuredPedestrians() {
		return injuredPedestrians;
	}
	public void setInjuredPedestrians(int injuredPedestrians) {
		this.injuredPedestrians = injuredPedestrians;
	}
	public int getKilledPedestrians() {
		return killedPedestrians;
	}
	public void setKilledPedestrians(int killedPedestrians) {
		this.killedPedestrians = killedPedestrians;
	}
	public int getInjuredCyclist() {
		return injuredCyclist;
	}
	public void setInjuredCyclist(int injuredCyclist) {
		this.injuredCyclist = injuredCyclist;
	}
	public int getKilledCyclist() {
		return killedCyclist;
	}
	public void setKilledCyclist(int killedCyclist) {
		this.killedCyclist = killedCyclist;
	}
	public int getInjuredMotorist() {
		return injuredMotorist;
	}
	public void setInjuredMotorist(int injuredMotorist) {
		this.injuredMotorist = injuredMotorist;
	}
	public int getKilledMotorist() {
		return killedMotorist;
	}
	public void setKilledMotorist(int killedMotorist) {
		this.killedMotorist = killedMotorist;
	}
	public String getContributingFactorVehicle1() {
		return contributingFactorVehicle1;
	}
	public void setContributingFactorVehicle1(String contributingFactorVehicle1) {
		this.contributingFactorVehicle1 = contributingFactorVehicle1;
	}
	public String getContributingFactorVehicle2() {
		return contributingFactorVehicle2;
	}
	public void setContributingFactorVehicle2(String contributingFactorVehicle2) {
		this.contributingFactorVehicle2 = contributingFactorVehicle2;
	}
	public String getContributingFactorVehicle3() {
		return contributingFactorVehicle3;
	}
	public void setContributingFactorVehicle3(String contributingFactorVehicle3) {
		this.contributingFactorVehicle3 = contributingFactorVehicle3;
	}
	public String getContributingFactorVehicle4() {
		return contributingFactorVehicle4;
	}
	public void setContributingFactorVehicle4(String contributingFactorVehicle4) {
		this.contributingFactorVehicle4 = contributingFactorVehicle4;
	}
	public String getContributingFactorVehicle5() {
		return contributingFactorVehicle5;
	}
	public void setContributingFactorVehicle5(String contributingFactorVehicle5) {
		this.contributingFactorVehicle5 = contributingFactorVehicle5;
	}
	public long getUniqueKey() {
		return uniqueKey;
	}
	public void setUniqueKey(long uniqueKey) {
		this.uniqueKey = uniqueKey;
	}
	public String getVehicleTypeCode1() {
		return vehicleTypeCode1;
	}
	public void setVehicleTypeCode1(String vehicleTypeCode1) {
		this.vehicleTypeCode1 = vehicleTypeCode1;
	}
	public String getVehicleTypeCode2() {
		return vehicleTypeCode2;
	}
	public void setVehicleTypeCode2(String vehicleTypeCode2) {
		this.vehicleTypeCode2 = vehicleTypeCode2;
	}
	public String getVehicleTypeCode3() {
		return vehicleTypeCode3;
	}
	public void setVehicleTypeCode3(String vehicleTypeCode3) {
		this.vehicleTypeCode3 = vehicleTypeCode3;
	}
	public String getVehicleTypeCode4() {
		return vehicleTypeCode4;
	}
	public void setVehicleTypeCode4(String vehicleTypeCode4) {
		this.vehicleTypeCode4 = vehicleTypeCode4;
	}
	public String getVehicleTypeCode5() {
		return vehicleTypeCode5;
	}
	public void setVehicleTypeCode5(String vehicleTypeCode5) {
		this.vehicleTypeCode5 = vehicleTypeCode5;
	}
}

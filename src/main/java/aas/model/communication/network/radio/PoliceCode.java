package aas.model.communication.network.radio;

public enum PoliceCode {
	
	OfficerOnDuty("OfficerOnDuty", 0),
	OfficerOffDuty("OfficerOffDuty", 1),
	CriminalDetected("CriminalDetected", 2),
	CriminalArrested("CriminalArrested", 3),
	ChaseCriminal("ChaseCriminal", 4),
	StopChase("StopChase", 5);
	
	private PoliceCode(final String name, final int ordinal) {}
	
}

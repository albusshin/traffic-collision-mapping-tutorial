package haven.mappingtutorial.db;

import java.util.ArrayList;
import java.util.List;

public class AdditionalConditions {
	public enum WithCondition{
		WithInjuries,
		WithDeaths,
		WithPedestriansInvolved,
		WithCyclistsInvolved,
		WithMotoristsInvolved;
	}
	public List<WithCondition> withConditions = new ArrayList<WithCondition>();
	public List<String> vehicleTypes = new ArrayList<String>();
	public List<String> contributingFactors = new ArrayList<String>();
	public int vehiclesInvolved = -1;
}
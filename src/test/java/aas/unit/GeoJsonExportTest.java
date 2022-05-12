package aas.unit;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import java.io.File;
import aas.model.util.Point;
import aas.controller.export.GeoJsonExport;

public class GeoJsonExportTest {
	
	@Test
	@Ignore
	public void testGeoJsonExport() {
		GeoJsonExport export = new GeoJsonExport("test.geojson");
		export.addPostion("test1", 0L, new Point(0.0, 0.0));
		export.addPostion("test1", 1L, new Point(1.0, 1.0));
		export.addPostion("test1", 2L, new Point(2.0, 1.0));
		export.addPostion("test1", 3L, new Point(3.0, 1.0));
		export.finish();
		assert new File("test.geojson").exists();
	}
	
	@Test
	@Ignore
	public void testAddPostion() {
		Assert.fail("Not yet implemented");
	}
	
	@Test
	@Ignore
	public void testFinish() {
		Assert.fail("Not yet implemented");
	}
	
}

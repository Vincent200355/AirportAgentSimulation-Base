package dhbw.sose2022.softwareengineering.airportagentsim.simulation.config;

public class PlacedEntityTest extends Object {
//    PlacedEntity testEntity;
//    int[] randomNumbers = new int[5];
//
//    @Before
//    public void setUp() throws Exception {
//        Random random = new Random();
//        randomNumbers = new int[5];
//
//        for (int i = 0; i < 5; i++) {
//            randomNumbers[i] = random.nextInt();
//        }
//
//        String jsonString = "    {\n" +
//                "      \"type\": \"" + randomNumbers[0] + "\",\n" +
//                "      \"position\": [\n" +
//                "        " + randomNumbers[1] + ",\n" +
//                "        " + randomNumbers[2] + " \n" +
//                "      ],\n" +
//                "      \"width\": " + randomNumbers[3] + ",\n" +
//                "      \"height\": " + randomNumbers[4] + ",\n" +
//                "      \"pluginAttributes\": {" +
//                "      \"value1\": \"156321\"" +
//                "      \"value2\": \"rmdam\" " +
//                "       }\n" +
//                "    }";
//        testEntity = new PlacedEntity(jsonString);
//    }
//
//    @Test
//    public void getAttribute() {
//        int width, height;
//        int[] position;
//        String type;
//
//        type = (String) testEntity.getAttribute("type");
//        width = (Integer) testEntity.getAttribute("width");
//        height = (Integer) testEntity.getAttribute("height");
//        position = (int[]) testEntity.getAttribute("position");
//
//        assertTrue("Type is incorrect!", type.equals(Integer.toString(randomNumbers[0])));
//        assertTrue("Width is incorrect!", width == randomNumbers[3]);
//        assertTrue("Height is incorrect!", height == randomNumbers[4]);
//        assertTrue("Position is incorrect!",
//                Arrays.equals(position, new int[]{randomNumbers[1], randomNumbers[2]}));
//    }
//
//    @Test
//    public void setAttribute() {
//        testEntity.setAttribute("position", new int[]{randomNumbers[0], randomNumbers[3]});
//
//        int[] position = (int[]) testEntity.getAttribute("position");
//
//        assertTrue("setAttribute doesn't worked!",
//                Arrays.equals(position, new int[]{randomNumbers[0], randomNumbers[3]}));
//    }
//
//    @Test
//    public void entityToString() {
//        System.out.println(testEntity.toString());
//    }
}

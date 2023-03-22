public class DatedMapTest {
    public static void main(String[] args) throws InterruptedException {
        DatedMap mapString = new DatedMapImpl();
        System.out.println(mapString.get("a1"));// Проверка метода при пустом mapString
        System.out.println(mapString.getKeyLastInsertionDate("a1"));// Проверка метода при пустом mapString
        mapString.put("a1", "asam");
        Thread.sleep(1000);
        mapString.put("b2", "bmw");
        Thread.sleep(1000);
        mapString.put("a3", "artefact");
        Thread.sleep(1000);
        mapString.put("b2", "bremen");
        System.out.println(mapString.keySet());
        System.out.println(mapString.get("a1"));
        System.out.println(mapString.getKeyLastInsertionDate("a1"));//


    }
}
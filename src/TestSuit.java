import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TestSuit {
    private static IronMan[] suits = {
            new IronMan(1, "Silver", "Silver"),
            new IronMan(2, "Red", "Yellow"),
            new IronMan(3, "Red", "Gold")
    };

    public static void main(String args[]) {

        List<IronMan> suitsList = Arrays.asList(suits);
        suitsList.forEach(suit -> System.out.println(suit.getVersion()));

        System.out.println("====================");
        List<IronMan> coloredSuits = suitsList.stream().filter(suit -> !suit.getMainColor().equals("Silver")).collect(Collectors.toList());

        coloredSuits.forEach(suit -> {
                    suit.setBeta(false);
                    System.out.println(suit.getVersion());
                }
        );
    }
}

import jdk.nashorn.internal.runtime.regexp.joni.ast.StringNode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestSplitMap {

    public static void main(String[] args){

        String taxCodes = "801:603;802:604;";

        List<String> taxCodeList = Arrays.asList(taxCodes.split(";"));
        Map<String, String> depositTaxes = new HashMap<>();
        taxCodeList.forEach( p -> {
            String[] pair = p.split(":");
            depositTaxes.put(pair[0], pair[1]);
        });

        System.out.println(depositTaxes);
    }

}



import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class LoopTest {

    public static void main(String args[]) {
        testMerge();
    }

    private static void testMerge() {
        Map<String, Object[]> oldTaxes = new HashMap<>();
        Map<String, Object[]> newTaxes = new HashMap<>();
        //String[] codes = {"110", "122", "211", "108", "315", "200", "203"};
        String[] codes = {"110", "122", "211"};
        //String[] codes = {"211", "125", "315", "200", "203"};
        double[] taxes = {100, 200, 100};
       // double[] taxes = {170.89, 4800, 300.69, 672, 2, 3, .5};
        //double[] taxes = {14.76, 14.05, 2, 3, .5};
        double[] pem = {100, 150, 180};
        //double[] pem = {287.1, 4800, 317.6, 552, 2, 3, .5};
        //double[] pem = {14.76, 8.41, 2, 3, .5};
        int counter = 0;
        for (String code : codes) {
            Object[] obj = new Object[3];
            obj[0] = taxes[counter];
            oldTaxes.put(code, obj);
            counter++;
        }
        counter = 0;
        for (String code : codes) {
            Object[] obj = new Object[3];
            obj[0] = pem[counter];
            newTaxes.put(code, obj);
            counter++;
        }
        Map<String, Object[]> merged = mergeTaxes2(newTaxes, oldTaxes);
        System.out.println("==========================");
        for (Map.Entry<String, Object[]> e : merged.entrySet()) {
            Map<String, BigDecimal> allocation = (Map <String, BigDecimal>) e.getValue()[2];

           if ((Double) e.getValue()[0] != 0 || allocation != null) {

                System.out.println("Cash: " + e.getKey() + " Tax: " + e.getValue()[0]);

                if (allocation != null) {
                    System.out.println(allocation);
                    for(Map.Entry<String, BigDecimal> entry : allocation.entrySet()){
                        System.out.println("MOP 98: " + entry.getKey() + " Value: " + entry.getValue().abs());
                    }

                }
                System.out.println("==========================");
            }

        }
    }

    public static Map<String, Object[]> mergeTaxes2(Map<String, Object[]> curTaxes, Map<String, Object[]> oldTaxes) {
        Map<String, Object[]> mergedTaxes = new HashMap<>();
        Object[] oldTaxInfo;
        Object[] curTaxInfo;
        Map<String, Object[]> negativeTaxes = new HashMap<>();
        //Merge Old Taxes and Current Taxes.
        for (Map.Entry<String, Object[]> taxAmtEntry : curTaxes.entrySet()) {
            String taxCode = taxAmtEntry.getKey();
            if (oldTaxes.get(taxCode) == null) {
                mergedTaxes.put(taxCode, taxAmtEntry.getValue());
            } else {
                oldTaxInfo = oldTaxes.get(taxCode);
                curTaxInfo = taxAmtEntry.getValue();
                double amt = new BigDecimal(curTaxInfo[0].toString()).subtract(new BigDecimal(oldTaxInfo[0].toString())).doubleValue();
                Object[] taxInfo = new Object[3];
                if (amt < 0.0d) {
                    negativeTaxes.put(taxCode, taxInfo);
                }
                taxInfo[0] = amt;
                taxInfo[1] = curTaxInfo[1];
                mergedTaxes.put(taxCode, taxInfo);
            }
        }
        Object[] newTaxInfo = new Object[3];
        for (Map.Entry<String, Object[]> taxAmtEntry : oldTaxes.entrySet()) {
            String taxCode = taxAmtEntry.getKey();
            if (mergedTaxes.get(taxCode) == null && !curTaxes.containsKey(taxCode)) {
                curTaxInfo = taxAmtEntry.getValue();
                Double amt = Double.parseDouble(curTaxInfo[0].toString()) * -1.0d;
                newTaxInfo[0] = amt;
                newTaxInfo[1] = curTaxInfo[1];

                if (amt < 0.0d) {
                    negativeTaxes.put(taxCode, newTaxInfo);
                }
                mergedTaxes.put(taxCode, newTaxInfo);
            }
        }

        negativeTaxes.forEach((taxCode, taxInfo) -> {
            double amt = new BigDecimal(taxInfo[0].toString()).doubleValue();
            Map.Entry<String, Object[]> entry = mergedTaxes.entrySet().stream().filter(m -> (Double) m.getValue()[0] + amt >= 0).findFirst().orElse(null);
            Map<String, BigDecimal> allocated = new HashMap<>();

            if (entry != null) {
                Object[] o = entry.getValue();
                o[0] = BigDecimal.valueOf((Double) o[0]).add(BigDecimal.valueOf(amt)).doubleValue();
                allocated.put(entry.getKey(), BigDecimal.valueOf(amt).abs());
            } else {
                BigDecimal currentAmount = BigDecimal.valueOf(amt);
                for (Map.Entry<String, Object[]> e : mergedTaxes.entrySet()) {
                    Object[] mergedInfo = e.getValue();
                    BigDecimal mergedAmount = new BigDecimal(mergedInfo[0].toString());
                    Map.Entry<String, Object[]> changed = mergedTaxes.entrySet().stream().filter(m -> (Double) m.getValue()[0] > 0).findFirst().orElse(null);

                    if (changed == null) {
                        break;
                    }
                    BigDecimal finalAmount = mergedAmount.add(currentAmount);
                    if (finalAmount.compareTo(BigDecimal.ZERO) < 0 && !mergedAmount.equals(BigDecimal.ZERO)) {
                        mergedInfo[0] = 0d;
                        currentAmount = finalAmount;

                        if (taxCode.equals(e.getKey())) {
                            mergedInfo[0] = amt;
                            break;
                        }

                        if (mergedAmount.compareTo(BigDecimal.ZERO) != 0) {
                            allocated.put(e.getKey(), mergedAmount);
                        }

                    } else if (currentAmount.compareTo(BigDecimal.ZERO) < 0) {
                        mergedInfo[0] = finalAmount.doubleValue();
                        currentAmount = finalAmount;
                        allocated.put(e.getKey(), mergedAmount.subtract(currentAmount.abs()).abs());
                    }
                }
            }
            taxInfo[2] = allocated;
        });

        mergedTaxes.putAll(negativeTaxes);
        return mergedTaxes;
    }
}

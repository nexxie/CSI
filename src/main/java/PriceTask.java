import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PriceTask {

    public Collection<Price> insert(Collection<Price> oldPrices, Collection<Price> newPrices) {

        List<Price> result = new ArrayList<>();

        Function<Price, List<Object>> compositeKey = price ->
                Arrays.asList(price.getProductCode(), price.getNumber(), price.getDepart());
        Map<Object, Set<Price>> currentMap =
                oldPrices.stream().collect(Collectors.groupingBy(compositeKey, Collectors.toSet()));
        Map<Object, Set<Price>> insertMap =
                newPrices.stream().collect(Collectors.groupingBy(compositeKey, Collectors.toSet()));

        Set<Object> keys = new HashSet<>(currentMap.keySet());
        Set<Object> newKeys = new HashSet<>(insertMap.keySet());
        /**
         * Обработка общих ключей
         */

        Set<Object> crossing = new HashSet<>(keys);

        crossing.retainAll(newKeys);

        for (Object key : crossing) {
            Set<Price> prices = currentMap.get(key);
            for (Price p : insertMap.get(key)) {
                prices = addNewPrice(prices, p);
            }
            result.addAll(prices);
        }

        /**
         * Удаление пустых интервалов
         */
        result = result.stream().filter(c -> !c.getEnd().equals(c.getBegin())).collect(Collectors.toList());
        /**
         * Значения уникальных ключей для обеих карт могут быть добавлены в результат без изменений
         */
        keys.removeAll(crossing);
        newKeys.removeAll(crossing);
        addPrices(result, currentMap, keys);
        addPrices(result, insertMap, newKeys);

        return result;
    }

    private void differentPriceRule(Collection<Price> result, Price oldPrice, Price newPrice) {
        Date oldStart = oldPrice.getBegin();
        Date newStart = newPrice.getBegin();
        Date oldEnd = oldPrice.getEnd();
        Date newEnd = newPrice.getEnd();

        if (oldStart.before(newStart)) {
            if (newEnd.before(oldEnd)) {

                Price cutPrice = new Price(oldPrice.getProductCode(), oldPrice.getNumber(),
                        oldPrice.getDepart(), newEnd, oldEnd, oldPrice.getValue());
                oldPrice.setEnd(newStart);
                addPrices(result, oldPrice, newPrice, cutPrice);
            } else {
                oldPrice.setEnd(newStart);
                addPrices(result, oldPrice, newPrice);
            }
        } else {
            if (newEnd.before(oldEnd)) {
                oldPrice.setBegin(newEnd);
                addPrices(result, oldPrice, newPrice);
            } else {
                result.add(newPrice);
            }
        }
    }

    private Set<Price> addNewPrice(Set<Price> oldSet, Price newPrice) {

        Set<Price> result = new HashSet<>();

        /**
         * Проверяем наличие пресечения цен
         */
        boolean commonDays = false;

        for (Price oldPrice : oldSet) {
            if (oldPrice.getBegin().after(newPrice.getEnd()) ||
                    oldPrice.getEnd().before(newPrice.getBegin())) {
                /**
                 * Новая цена не влияет на старую цену. Добавляем её к
                 * @param  result
                 */
                result.add(oldPrice);
            } else {
                /**
                 */
                commonDays = true;
                if (newPrice.getValue() == oldPrice.getValue()) {
                    result.add(priceRule(oldPrice, newPrice));
                } else {
                    /**
                     * Разные значения
                     * */
                    differentPriceRule(result, oldPrice, newPrice);
                }
            }
        }
        /**
         * Новая цена для
         * @param newPrice
         */

        if (!commonDays) {
            result.add(newPrice);
        }
        return result;
    }

    private Price priceRule(Price oldPrice, Price newPrice) {
        if (oldPrice.getBegin().after(newPrice.getBegin())) {
            oldPrice.setBegin(newPrice.getBegin());
        }
        if (oldPrice.getEnd().before(newPrice.getEnd())) {
            oldPrice.setEnd(newPrice.getEnd());
        }

        newPrice.setBegin(oldPrice.getEnd());

        return oldPrice;
    }

    private void addPrices(Collection<Price> result, Price... prices) {
        for (int i = 0; i < prices.length; i++) {
            result.add(prices[i]);
        }
    }

    private void addPrices(Collection<Price> result, Map<Object, Set<Price>> map, Set<Object> keySet) {
        for (Object key : keySet) {
            result.addAll(map.get(key));
        }
    }

}
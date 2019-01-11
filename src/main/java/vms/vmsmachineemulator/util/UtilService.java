package vms.vmsmachineemulator.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class UtilService {

  public static int getRandomNumber(int from, int to) {
    return new Random().ints(1, from, to).findFirst().getAsInt();
  }

  public static <T> List<T> mergeLists(List<T> ...lists) {
    List<T> res = new ArrayList<T>();
    for (List<T> list: lists) {
      list.forEach(t -> res.add(t));
    }
    return res;
  }
  public static <T> Set<T> mergeSets(Set<T> ...sets) {
    Set<T> res = new HashSet<T>();
    for (Set<T> set: sets) {
      set.forEach(t -> res.add(t));
    }
    return res;
  }
}
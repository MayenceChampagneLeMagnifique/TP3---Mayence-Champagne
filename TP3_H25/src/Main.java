import reseau.CivixNet;
import reseau.ReseauBuilder;

public class Main {
    public static void main(String[] args) throws Exception {
        CivixNet reseau = ReseauBuilder.chargerDepuisJSON("src\\donnees\\reseau.json");
        System.out.println(reseau);

        ReseauBuilder.serialise(reseau, "src\\donnees\\");
        CivixNet reseau2 = ReseauBuilder.deserialise("src\\donnees\\civixNet.ser");
        System.out.println(reseau2);

        System.out.println(reseau.propagationFausseInformationRecursive("alice"));
    }
}
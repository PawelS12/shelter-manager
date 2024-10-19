public class Animal implements Comparable<Animal> {
    private String name;
    private String species;
    private AnimalCondition condition;
    private int age;
    private double price;
    private ComparisonType comparisonType;

    public Animal(String name, String species, AnimalCondition condition, int age, double price, ComparisonType comparisonType) {
        this.name = name;
        this.species = species;
        this.condition = condition;
        this.age = age;
        this.price = price;
        this.comparisonType = comparisonType;
    }

    public void Print() {
        System.out.println("Animal: " + name + ", " + species + ", " + condition + ", " + age + ", " + price );
    }

    @Override
    public int compareTo(Animal other) {
        if (other == null) {
            return 1;
        }

        switch (comparisonType) {
            case NAME:
                return this.name.compareTo(other.name);
            case SPECIES:
                return this.species.compareTo(other.species);
            case AGE:
                return Integer.compare(this.age, other.age);
            default:
                throw new IllegalStateException("Unexpected value: " + comparisonType);
        }
    }
}
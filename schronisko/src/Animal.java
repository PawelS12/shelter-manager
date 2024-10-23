public class Animal implements Comparable<Animal> {
    private String name;
    private String species;
    private AnimalCondition condition;
    private int age;
    private double price;
    private boolean isAdopted;

    public Animal(String name, String species, AnimalCondition condition, int age, double price) {
        this.name = name;
        this.species = species;
        this.condition = condition;
        this.age = age;
        this.price = price;
        this.isAdopted = false;
    }

    public void setAdopted() {
        isAdopted = true;
    }

    public void setCondition(AnimalCondition condition) {
        this.condition = condition;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public AnimalCondition getCondition() {
        return this.condition;
    }

    public String getName() {
        return this.name;
    }

    public double getPrice() {
        return this.price;
    }

    public String getSpecies() {
        return this.species;
    }

    public void Print() {
        System.out.println("Imię: " + name + " | gatunek: " + species + " | stan: " + condition + " | wiek: " + age + " | cena: " + price );
    }

    @Override
    public int compareTo(Animal other) {
        if (other == null) {
            return 1;
        }

        int nameComparison = this.name.compareTo(other.name);
        if (nameComparison != 0) {
            return nameComparison;
        }

        int speciesComparison = this.species.compareTo(other.species);
        if (speciesComparison != 0) {
            return speciesComparison;
        }

        return Integer.compare(this.age, other.age);
    }

}
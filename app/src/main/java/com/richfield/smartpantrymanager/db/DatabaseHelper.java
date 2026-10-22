package com.richfield.smartpantrymanager.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.richfield.smartpantrymanager.model.Ingredient;
import com.richfield.smartpantrymanager.model.Recipe;
import com.richfield.smartpantrymanager.model.RecipeIngredient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "smart_pantry.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_PANTRY = "pantry";
    public static final String COL_PANTRY_ID = "id";
    public static final String COL_PANTRY_NAME = "name";
    public static final String COL_PANTRY_QUANTITY = "quantity";
    public static final String COL_PANTRY_UNIT = "unit";
    public static final String COL_PANTRY_EXPIRY = "expiry_date";

    public static final String TABLE_RECIPES = "recipes";
    public static final String COL_RECIPE_ID = "id";
    public static final String COL_RECIPE_NAME = "name";
    public static final String COL_RECIPE_DESC = "description";
    public static final String COL_RECIPE_INSTRUCTIONS = "instructions";
    public static final String COL_RECIPE_PREP = "prep_time";
    public static final String COL_RECIPE_COOK = "cook_time";
    public static final String COL_RECIPE_SERVINGS = "servings";
    public static final String COL_RECIPE_CATEGORY = "category";

    public static final String TABLE_RECIPE_INGREDIENTS = "recipe_ingredients";
    public static final String COL_RI_ID = "id";
    public static final String COL_RI_RECIPE_ID = "recipe_id";
    public static final String COL_RI_NAME = "ingredient_name";
    public static final String COL_RI_QUANTITY = "quantity";
    public static final String COL_RI_UNIT = "unit";

    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createPantry = "CREATE TABLE " + TABLE_PANTRY + " (" +
                COL_PANTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_PANTRY_NAME + " TEXT NOT NULL, " +
                COL_PANTRY_QUANTITY + " REAL NOT NULL, " +
                COL_PANTRY_UNIT + " TEXT NOT NULL, " +
                COL_PANTRY_EXPIRY + " TEXT)";
        db.execSQL(createPantry);

        String createRecipes = "CREATE TABLE " + TABLE_RECIPES + " (" +
                COL_RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_RECIPE_NAME + " TEXT NOT NULL UNIQUE, " +
                COL_RECIPE_DESC + " TEXT, " +
                COL_RECIPE_INSTRUCTIONS + " TEXT, " +
                COL_RECIPE_PREP + " TEXT, " +
                COL_RECIPE_COOK + " TEXT, " +
                COL_RECIPE_SERVINGS + " TEXT, " +
                COL_RECIPE_CATEGORY + " TEXT)";
        db.execSQL(createRecipes);

        String createRecipeIngredients = "CREATE TABLE " + TABLE_RECIPE_INGREDIENTS + " (" +
                COL_RI_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_RI_RECIPE_ID + " INTEGER NOT NULL, " +
                COL_RI_NAME + " TEXT NOT NULL, " +
                COL_RI_QUANTITY + " REAL NOT NULL, " +
                COL_RI_UNIT + " TEXT NOT NULL, " +
                "FOREIGN KEY(" + COL_RI_RECIPE_ID + ") REFERENCES " + TABLE_RECIPES + "(" + COL_RECIPE_ID + "))";
        db.execSQL(createRecipeIngredients);

        seedRecipes(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE_INGREDIENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PANTRY);
        onCreate(db);
    }

    private void seedRecipes(SQLiteDatabase db) {
        insertRecipe(db, "Pap with Chakalaka",
                "A classic South African comfort meal of soft maize porridge served with spicy vegetable relish.",
                "1. Boil water with salt. 2. Slowly stir in maize meal until thick. 3. Simmer 20 minutes. 4. Fry onion, garlic, chilli. 5. Add chopped tomatoes, beans, carrots and spices. 6. Simmer until thick. Serve pap topped with chakalaka.",
                "10 min", "35 min", "4", "Main",
                new String[]{"maize meal", "water", "onion", "tomato", "baked beans", "carrot", "chilli", "garlic", "oil", "salt"},
                new double[]{500, 1000, 1, 4, 400, 2, 1, 2, 30, 5},
                new String[]{"g", "ml", "pcs", "pcs", "g", "pcs", "pcs", "pcs", "ml", "g"});

        insertRecipe(db, "Umngqusho",
                "Traditional Xhosa dish of samp and sugar beans slow-cooked until soft and creamy.",
                "1. Soak samp and beans overnight. 2. Drain and boil in fresh water until soft (2-3 hours). 3. Add salt, oil and optional onion. 4. Mash lightly. Serve hot.",
                "15 min + soak", "180 min", "6", "Main",
                new String[]{"samp", "sugar beans", "onion", "oil", "salt", "water"},
                new double[]{400, 300, 1, 30, 10, 1500},
                new String[]{"g", "g", "pcs", "ml", "g", "ml"});

        insertRecipe(db, "Tomato Bredie",
                "Slow-cooked Cape Malay tomato and mutton stew, fragrant with spices.",
                "1. Brown mutton pieces. 2. Fry onion, garlic, ginger. 3. Add tomatoes, spices and meat. 4. Simmer covered for 90 minutes until tender. 5. Adjust seasoning and serve with rice or bread.",
                "20 min", "100 min", "6", "Main",
                new String[]{"mutton", "onion", "tomato", "garlic", "ginger", "potato", "oil", "salt", "pepper"},
                new double[]{800, 2, 6, 3, 20, 3, 30, 8, 2},
                new String[]{"g", "pcs", "pcs", "pcs", "g", "pcs", "ml", "g", "g"});

        insertRecipe(db, "Bobotie",
                "Cape Malay classic of spiced minced meat topped with egg custard and baked.",
                "1. Fry onion and mince. 2. Add curry powder, chutney, raisins, bread soaked in milk. 3. Place in baking dish. 4. Pour beaten eggs mixed with milk over. 5. Bake at 180°C for 40 minutes until set.",
                "25 min", "45 min", "6", "Main",
                new String[]{"mince", "onion", "egg", "milk", "bread", "raisins", "chutney", "curry powder", "oil", "salt"},
                new double[]{700, 2, 3, 250, 2, 50, 60, 15, 20, 5},
                new String[]{"g", "pcs", "pcs", "ml", "pcs", "g", "ml", "g", "ml", "g"});

        insertRecipe(db, "Potjiekos",
                "Traditional three-legged pot stew layered with meat and vegetables, cooked slowly over coals.",
                "1. Brown meat in potjie. 2. Layer onions, potatoes, carrots, cabbage. 3. Add stock and spices. 4. Cover and simmer gently for 2-3 hours without stirring. Serve with rice or pap.",
                "30 min", "180 min", "8", "Main",
                new String[]{"beef", "onion", "potato", "carrot", "cabbage", "stock", "oil", "salt", "pepper"},
                new double[]{1000, 3, 4, 3, 0.5, 500, 40, 10, 3},
                new String[]{"g", "pcs", "pcs", "pcs", "pcs", "ml", "ml", "g", "g"});

        insertRecipe(db, "Samp and Beans",
                "Hearty everyday staple of boiled samp and beans seasoned simply.",
                "1. Soak samp and beans. 2. Boil together until soft. 3. Season with salt, oil and optional margarine. Serve as a side or main.",
                "10 min + soak", "150 min", "6", "Main",
                new String[]{"samp", "sugar beans", "oil", "salt", "water"},
                new double[]{350, 250, 25, 8, 1200},
                new String[]{"g", "g", "ml", "g", "ml"});

        insertRecipe(db, "Vetkoek",
                "Deep-fried dough balls, often filled with mince or jam – a South African favourite.",
                "1. Mix flour, yeast, sugar, salt and warm water into soft dough. 2. Rise 1 hour. 3. Shape into balls and deep-fry until golden. 4. Drain and serve plain or filled.",
                "20 min + rise", "30 min", "12", "Snack",
                new String[]{"flour", "yeast", "sugar", "salt", "water", "oil"},
                new double[]{500, 10, 20, 5, 300, 500},
                new String[]{"g", "g", "g", "g", "ml", "ml"});

        insertRecipe(db, "Boerewors with Pap",
                "Grilled traditional sausage served with creamy maize porridge.",
                "1. Grill or braai boerewors until cooked through. 2. Prepare soft pap with maize meal, water and salt. 3. Serve sausage sliced over pap with tomato relish if desired.",
                "10 min", "30 min", "4", "Main",
                new String[]{"boerewors", "maize meal", "water", "salt", "oil"},
                new double[]{600, 400, 900, 5, 15},
                new String[]{"g", "g", "ml", "g", "ml"});

        insertRecipe(db, "Chicken Curry",
                "Mild South African-style chicken curry with aromatic spices and potato.",
                "1. Brown chicken pieces. 2. Fry onion, garlic, ginger and curry spices. 3. Add tomatoes, potato and chicken. 4. Simmer until chicken is tender and sauce thickens. Serve with rice.",
                "20 min", "50 min", "5", "Main",
                new String[]{"chicken", "onion", "tomato", "potato", "garlic", "ginger", "curry powder", "oil", "salt"},
                new double[]{800, 2, 3, 2, 3, 15, 20, 30, 6},
                new String[]{"g", "pcs", "pcs", "pcs", "pcs", "g", "g", "ml", "g"});

        insertRecipe(db, "Morogo with Pap",
                "Wild spinach or amaranth leaves cooked simply and served with pap.",
                "1. Wash morogo thoroughly. 2. Fry onion in oil. 3. Add morogo and a little water, steam until soft. 4. Season. Serve with soft pap.",
                "15 min", "25 min", "4", "Main",
                new String[]{"morogo", "onion", "oil", "salt", "maize meal", "water"},
                new double[]{400, 1, 25, 4, 350, 800},
                new String[]{"g", "pcs", "ml", "g", "g", "ml"});

        insertRecipe(db, "Butternut Soup",
                "Smooth, lightly spiced butternut soup perfect for cooler evenings.",
                "1. Sauté onion and garlic. 2. Add cubed butternut and stock. 3. Simmer until soft. 4. Blend smooth, season and finish with a splash of cream if available.",
                "15 min", "35 min", "4", "Starter",
                new String[]{"butternut", "onion", "garlic", "stock", "oil", "salt", "pepper"},
                new double[]{800, 1, 2, 700, 20, 5, 2},
                new String[]{"g", "pcs", "pcs", "ml", "ml", "g", "g"});

        insertRecipe(db, "Baked Fish with Vegetables",
                "Simple oven-baked fish fillets with seasonal vegetables.",
                "1. Place fish in baking dish. 2. Surround with sliced potato, carrot and onion. 3. Drizzle oil, season, add lemon. 4. Bake at 180°C for 25-30 minutes.",
                "15 min", "30 min", "4", "Main",
                new String[]{"fish", "potato", "carrot", "onion", "oil", "lemon", "salt", "pepper"},
                new double[]{600, 3, 2, 1, 30, 1, 5, 2},
                new String[]{"g", "pcs", "pcs", "pcs", "ml", "pcs", "g", "g"});

        insertRecipe(db, "Bean Stew",
                "Hearty vegetarian bean stew thickened with potato and flavoured with herbs.",
                "1. Soak and cook dried beans or use canned. 2. Fry onion and garlic. 3. Add beans, potato, tomato, stock and herbs. 4. Simmer until thick and flavoursome.",
                "15 min", "40 min", "5", "Main",
                new String[]{"sugar beans", "onion", "potato", "tomato", "garlic", "stock", "oil", "salt"},
                new double[]{400, 2, 2, 3, 2, 400, 25, 6},
                new String[]{"g", "pcs", "pcs", "pcs", "pcs", "ml", "ml", "g"});

        insertRecipe(db, "Beef Stew",
                "Classic slow-cooked beef stew with root vegetables.",
                "1. Brown beef cubes. 2. Fry onion. 3. Add carrots, potato, stock and herbs. 4. Simmer covered until meat is tender (90-120 min). Thicken if needed.",
                "20 min", "120 min", "6", "Main",
                new String[]{"beef", "onion", "carrot", "potato", "stock", "oil", "salt", "pepper"},
                new double[]{800, 2, 3, 3, 600, 30, 8, 3},
                new String[]{"g", "pcs", "pcs", "pcs", "ml", "ml", "g", "g"});

        insertRecipe(db, "Vegetable Curry",
                "Mild mixed vegetable curry suitable for everyday meals.",
                "1. Fry onion, garlic and curry spices. 2. Add potato, carrot, cauliflower and tomato. 3. Add a little water and simmer until vegetables are tender. Serve with rice or bread.",
                "15 min", "35 min", "4", "Main",
                new String[]{"potato", "carrot", "onion", "tomato", "garlic", "curry powder", "oil", "salt"},
                new double[]{3, 2, 1, 2, 2, 15, 25, 5},
                new String[]{"pcs", "pcs", "pcs", "pcs", "pcs", "g", "ml", "g"});

        insertRecipe(db, "Potato Salad",
                "Creamy potato salad with egg and mayonnaise, a braai side staple.",
                "1. Boil potatoes until just soft. 2. Cool and cube. 3. Mix with chopped boiled eggs, onion and mayonnaise. 4. Season and chill before serving.",
                "15 min", "25 min", "6", "Side",
                new String[]{"potato", "egg", "onion", "mayonnaise", "salt", "pepper"},
                new double[]{800, 4, 1, 150, 4, 2},
                new String[]{"g", "pcs", "pcs", "ml", "g", "g"});

        insertRecipe(db, "Pumpkin Fritters",
                "Sweet pumpkin fritters dusted with cinnamon sugar – a traditional dessert.",
                "1. Mash cooked pumpkin. 2. Mix with flour, egg, sugar and baking powder. 3. Fry spoonfuls in hot oil until golden. 4. Drain and dust with cinnamon sugar.",
                "20 min", "20 min", "8", "Dessert",
                new String[]{"pumpkin", "flour", "egg", "sugar", "baking powder", "oil", "cinnamon"},
                new double[]{400, 150, 1, 40, 5, 200, 5},
                new String[]{"g", "g", "pcs", "g", "g", "ml", "g"});

        insertRecipe(db, "Savoury Mince",
                "Everyday savoury mince with vegetables, perfect over rice or pap.",
                "1. Fry onion and mince until browned. 2. Add carrot, tomato, stock and seasoning. 3. Simmer 25 minutes. Serve hot.",
                "10 min", "30 min", "4", "Main",
                new String[]{"mince", "onion", "carrot", "tomato", "stock", "oil", "salt"},
                new double[]{500, 1, 2, 2, 200, 20, 5},
                new String[]{"g", "pcs", "pcs", "pcs", "ml", "ml", "g"});

        insertRecipe(db, "Chicken and Vegetable Stew",
                "Light stew of chicken pieces with mixed vegetables.",
                "1. Brown chicken. 2. Add onion, carrot, potato and stock. 3. Simmer until chicken is cooked and vegetables soft. Season to taste.",
                "15 min", "45 min", "5", "Main",
                new String[]{"chicken", "onion", "carrot", "potato", "stock", "oil", "salt"},
                new double[]{700, 1, 2, 2, 500, 25, 6},
                new String[]{"g", "pcs", "pcs", "pcs", "ml", "ml", "g"});

        insertRecipe(db, "Cabbage and Potato Stew",
                "Budget-friendly vegetable stew of cabbage and potato.",
                "1. Fry onion. 2. Add cubed potato and shredded cabbage with a little water. 3. Cover and simmer until soft. Season with salt and pepper.",
                "10 min", "30 min", "4", "Main",
                new String[]{"cabbage", "potato", "onion", "oil", "salt", "pepper"},
                new double[]{0.5, 3, 1, 20, 5, 2},
                new String[]{"pcs", "pcs", "pcs", "ml", "g", "g"});
    }

    private void insertRecipe(SQLiteDatabase db, String name, String desc, String instructions,
                              String prep, String cook, String servings, String category,
                              String[] ingredients, double[] quantities, String[] units) {
        ContentValues cv = new ContentValues();
        cv.put(COL_RECIPE_NAME, name);
        cv.put(COL_RECIPE_DESC, desc);
        cv.put(COL_RECIPE_INSTRUCTIONS, instructions);
        cv.put(COL_RECIPE_PREP, prep);
        cv.put(COL_RECIPE_COOK, cook);
        cv.put(COL_RECIPE_SERVINGS, servings);
        cv.put(COL_RECIPE_CATEGORY, category);
        long recipeId = db.insert(TABLE_RECIPES, null, cv);

        for (int i = 0; i < ingredients.length; i++) {
            ContentValues ri = new ContentValues();
            ri.put(COL_RI_RECIPE_ID, recipeId);
            ri.put(COL_RI_NAME, ingredients[i]);
            ri.put(COL_RI_QUANTITY, quantities[i]);
            ri.put(COL_RI_UNIT, units[i]);
            db.insert(TABLE_RECIPE_INGREDIENTS, null, ri);
        }
    }

    public long addIngredient(Ingredient ingredient) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_PANTRY_NAME, ingredient.getName().trim());
        cv.put(COL_PANTRY_QUANTITY, ingredient.getQuantity());
        cv.put(COL_PANTRY_UNIT, ingredient.getUnit());
        cv.put(COL_PANTRY_EXPIRY, ingredient.getExpiryDate());
        long id = db.insert(TABLE_PANTRY, null, cv);
        return id;
    }

    public int updateIngredient(Ingredient ingredient) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_PANTRY_NAME, ingredient.getName().trim());
        cv.put(COL_PANTRY_QUANTITY, ingredient.getQuantity());
        cv.put(COL_PANTRY_UNIT, ingredient.getUnit());
        cv.put(COL_PANTRY_EXPIRY, ingredient.getExpiryDate());
        return db.update(TABLE_PANTRY, cv, COL_PANTRY_ID + "=?", new String[]{String.valueOf(ingredient.getId())});
    }

    public int deleteIngredient(long id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_PANTRY, COL_PANTRY_ID + "=?", new String[]{String.valueOf(id)});
    }

    public List<Ingredient> getAllIngredients() {
        List<Ingredient> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_PANTRY, null, null, null, null, null, COL_PANTRY_NAME + " COLLATE NOCASE ASC");
        if (c != null) {
            while (c.moveToNext()) {
                Ingredient ing = new Ingredient(
                        c.getLong(c.getColumnIndexOrThrow(COL_PANTRY_ID)),
                        c.getString(c.getColumnIndexOrThrow(COL_PANTRY_NAME)),
                        c.getDouble(c.getColumnIndexOrThrow(COL_PANTRY_QUANTITY)),
                        c.getString(c.getColumnIndexOrThrow(COL_PANTRY_UNIT)),
                        c.getString(c.getColumnIndexOrThrow(COL_PANTRY_EXPIRY))
                );
                list.add(ing);
            }
            c.close();
        }
        return list;
    }

    public Ingredient getIngredientById(long id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_PANTRY, null, COL_PANTRY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        Ingredient ing = null;
        if (c != null && c.moveToFirst()) {
            ing = new Ingredient(
                    c.getLong(c.getColumnIndexOrThrow(COL_PANTRY_ID)),
                    c.getString(c.getColumnIndexOrThrow(COL_PANTRY_NAME)),
                    c.getDouble(c.getColumnIndexOrThrow(COL_PANTRY_QUANTITY)),
                    c.getString(c.getColumnIndexOrThrow(COL_PANTRY_UNIT)),
                    c.getString(c.getColumnIndexOrThrow(COL_PANTRY_EXPIRY))
            );
            c.close();
        }
        return ing;
    }

    public int getIngredientCount() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_PANTRY, null);
        int count = 0;
        if (c != null && c.moveToFirst()) {
            count = c.getInt(0);
            c.close();
        }
        return count;
    }

    public List<Recipe> getAllRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_RECIPES, null, null, null, null, null, COL_RECIPE_NAME + " ASC");
        if (c != null) {
            while (c.moveToNext()) {
                Recipe r = cursorToRecipe(c);
                r.setIngredients(getRecipeIngredients(r.getId()));
                recipes.add(r);
            }
            c.close();
        }
        return recipes;
    }

    public Recipe getRecipeById(long id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_RECIPES, null, COL_RECIPE_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        Recipe r = null;
        if (c != null && c.moveToFirst()) {
            r = cursorToRecipe(c);
            r.setIngredients(getRecipeIngredients(r.getId()));
            c.close();
        }
        return r;
    }

    private Recipe cursorToRecipe(Cursor c) {
        return new Recipe(
                c.getLong(c.getColumnIndexOrThrow(COL_RECIPE_ID)),
                c.getString(c.getColumnIndexOrThrow(COL_RECIPE_NAME)),
                c.getString(c.getColumnIndexOrThrow(COL_RECIPE_DESC)),
                c.getString(c.getColumnIndexOrThrow(COL_RECIPE_INSTRUCTIONS)),
                c.getString(c.getColumnIndexOrThrow(COL_RECIPE_PREP)),
                c.getString(c.getColumnIndexOrThrow(COL_RECIPE_COOK)),
                c.getString(c.getColumnIndexOrThrow(COL_RECIPE_SERVINGS)),
                c.getString(c.getColumnIndexOrThrow(COL_RECIPE_CATEGORY))
        );
    }

    public List<RecipeIngredient> getRecipeIngredients(long recipeId) {
        List<RecipeIngredient> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_RECIPE_INGREDIENTS, null, COL_RI_RECIPE_ID + "=?",
                new String[]{String.valueOf(recipeId)}, null, null, null);
        if (c != null) {
            while (c.moveToNext()) {
                list.add(new RecipeIngredient(
                        c.getLong(c.getColumnIndexOrThrow(COL_RI_ID)),
                        c.getLong(c.getColumnIndexOrThrow(COL_RI_RECIPE_ID)),
                        c.getString(c.getColumnIndexOrThrow(COL_RI_NAME)),
                        c.getDouble(c.getColumnIndexOrThrow(COL_RI_QUANTITY)),
                        c.getString(c.getColumnIndexOrThrow(COL_RI_UNIT))
                ));
            }
            c.close();
        }
        return list;
    }

    public List<Recipe> getSuggestedRecipes() {
        List<Recipe> all = getAllRecipesWithAvailability();
        List<Recipe> suggested = new ArrayList<>();
        for (Recipe recipe : all) {
            if (recipe.isAvailable()) {
                suggested.add(recipe);
            }
        }
        return suggested;
    }

    public List<Recipe> getAllRecipesWithAvailability() {
        List<Ingredient> pantry = getAllIngredients();
        Map<String, Double> pantryMap = buildNormalizedPantryMap(pantry);
        List<Recipe> all = getAllRecipes();

        for (Recipe recipe : all) {
            List<String> missing = new ArrayList<>();
            boolean fullyAvailable = true;
            for (RecipeIngredient ri : recipe.getIngredients()) {
                String key = normalizeName(ri.getIngredientName());
                double needed = convertToBase(ri.getQuantity(), ri.getUnit());
                Double available = pantryMap.get(key);
                if (available == null || available < needed - 0.001) {
                    fullyAvailable = false;
                    String miss = ri.getDisplay();
                    if (available != null) {
                        miss += " (have " + String.format(Locale.US, "%.1f", available) + " base units)";
                    }
                    missing.add(miss);
                }
            }
            recipe.setAvailable(fullyAvailable);
            recipe.setMissingItems(missing);
        }
        return all;
    }


    private Map<String, Double> buildNormalizedPantryMap(List<Ingredient> pantry) {
        Map<String, Double> map = new HashMap<>();
        for (Ingredient ing : pantry) {
            String key = normalizeName(ing.getName());
            double baseQty = convertToBase(ing.getQuantity(), ing.getUnit());
            if (map.containsKey(key)) {
                map.put(key, map.get(key) + baseQty);
            } else {
                map.put(key, baseQty);
            }
        }
        return map;
    }

    public static String normalizeName(String name) {
        if (name == null) return "";
        String n = name.toLowerCase(Locale.US).trim();
        if (n.endsWith("oes")) {
            n = n.substring(0, n.length() - 2);
        } else if (n.endsWith("ies")) {
            n = n.substring(0, n.length() - 3) + "y";
        } else if (n.endsWith("s") && n.length() > 3 && !n.endsWith("ss")) {
            n = n.substring(0, n.length() - 1);
        }
        n = n.replace("maize meal", "maize meal")
                .replace("mealie meal", "maize meal")
                .replace("sugar bean", "sugar beans")
                .replace("baked bean", "baked beans");
        return n;
    }

    public static double convertToBase(double quantity, String unit) {
        if (unit == null) return quantity;
        String u = unit.toLowerCase(Locale.US).trim();
        switch (u) {
            case "kg":
            case "kilogram":
            case "kilograms":
                return quantity * 1000;
            case "g":
            case "gram":
            case "grams":
                return quantity;
            case "l":
            case "litre":
            case "liter":
            case "litres":
            case "liters":
                return quantity * 1000;
            case "ml":
            case "millilitre":
            case "milliliter":
            case "millilitres":
            case "milliliters":
                return quantity;
            case "pcs":
            case "pc":
            case "piece":
            case "pieces":
            case "item":
            case "items":
            case "unit":
            case "units":
                return quantity;
            default:
                return quantity;
        }
    }
}

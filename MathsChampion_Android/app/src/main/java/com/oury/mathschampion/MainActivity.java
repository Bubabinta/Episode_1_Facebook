package com.oury.mathschampion;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MainActivity extends Activity {
    private final Random rnd = new Random();
    private LinearLayout root;
    private SharedPreferences prefs;
    private String name = "", mode = "MIX";
    private boolean atNameEntry = false;
    private int level = 1, stars = 0, total = 0, correct = 0, q = 0, roundCorrect = 0, answer = 0;
    private static final int PURPLE = Color.rgb(91,75,219), PINK = Color.rgb(255,99,174), BLUE = Color.rgb(57,173,255), GREEN = Color.rgb(63,190,126), ORANGE = Color.rgb(255,164,66), BG = Color.rgb(246,247,252), TEXT = Color.rgb(37,41,57);

    @Override public void onCreate(Bundle b) {
        super.onCreate(b);
        prefs = getSharedPreferences("maths_champion", MODE_PRIVATE);
        level = prefs.getInt("level",1);
        stars = prefs.getInt("stars",0);
        total = prefs.getInt("total",0);
        correct = prefs.getInt("correct",0);
        nameEntry();
    }

    private void screen() {
        ScrollView s = new ScrollView(this); s.setBackgroundColor(BG); s.setFillViewport(true);
        root = new LinearLayout(this); root.setOrientation(LinearLayout.VERTICAL); root.setPadding(dp(20),dp(22),dp(20),dp(30));
        s.addView(root); setContentView(s);
    }

    private TextView text(String value, int size, boolean bold) {
        TextView t = new TextView(this); t.setText(value); t.setTextSize(size); t.setTextColor(TEXT); t.setGravity(Gravity.CENTER); t.setPadding(4,dp(8),4,dp(8));
        if (bold) t.setTypeface(Typeface.DEFAULT, Typeface.BOLD); root.addView(t); return t;
    }

    private Button button(String label, int color) {
        Button b = new Button(this); b.setText(label); b.setTextSize(18); b.setAllCaps(false); b.setTextColor(Color.WHITE); b.setTypeface(Typeface.DEFAULT, Typeface.BOLD); b.setBackgroundTintList(android.content.res.ColorStateList.valueOf(color));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1,dp(60)); lp.setMargins(0,dp(6),0,dp(6)); root.addView(b,lp); return b;
    }

    private void nameEntry() {
        atNameEntry = true;
        screen();
        text("🧠 TafCalcul",32,true);
        text("Le jeu de calcul mental pour devenir plus rapide chaque jour !",16,false);
        text("Entre ton nom avant de commencer",22,true);
        EditText e = new EditText(this);
        e.setHint("Ton prénom / nom");
        e.setTextSize(19);
        e.setSingleLine(true);
        root.addView(e,new LinearLayout.LayoutParams(-1,dp(64)));
        button("▶ Commencer",PURPLE).setOnClickListener(v -> {
            String n = e.getText().toString().trim();
            if (n.isEmpty()) {
                Toast.makeText(this,"Entre ton nom pour continuer",Toast.LENGTH_SHORT).show();
                e.requestFocus();
                return;
            }
            name = n;
            save();
            home();
        });
    }

    private void home() {
        atNameEntry = false;
        screen(); text("🧠 TafCalcul",30,true); text("Apprends, joue et deviens champion du calcul mental !",16,false); text("Bonjour " + name + " 👋",22,true); text("Niveau " + level + "   •   ⭐ " + stars,17,false);
        button("▶ Jouer",PURPLE).setOnClickListener(v -> menu());
        button("🎯 Défi du jour",PINK).setOnClickListener(v -> start("MIX"));
        button("🏆 Trophées",ORANGE).setOnClickListener(v -> trophies());
        button("📊 Progression",GREEN).setOnClickListener(v -> progress());
        button("👤 Profil",BLUE).setOnClickListener(v -> profile());
    }

    private void menu() {
        screen(); text("Choisis ton défi",28,true);
        button("➕ Addition",BLUE).setOnClickListener(v -> start("ADD"));
        button("➖ Soustraction",PINK).setOnClickListener(v -> start("SUB"));
        button("✖ Multiplication",PURPLE).setOnClickListener(v -> start("MUL"));
        button("➗ Division",GREEN).setOnClickListener(v -> start("DIV"));
        button("🎲 Mélange",ORANGE).setOnClickListener(v -> start("MIX"));
        button("← Retour",Color.DKGRAY).setOnClickListener(v -> home());
    }

    private void start(String m) { mode=m; q=0; roundCorrect=0; next(); }

    private void next() {
        if (q >= 10) { finishRound(); return; }
        screen(); text("Question " + (q+1) + " / 10",18,true); text(name + "   •   ⭐ " + stars + "   •   ✅ " + roundCorrect,16,false);
        String op = mode; if ("MIX".equals(op)) { String[] ops={"ADD","SUB","MUL","DIV"}; op=ops[rnd.nextInt(4)]; }
        int a,b; String sign;
        if ("SUB".equals(op)) { a=rnd.nextInt(20+level)+1; b=rnd.nextInt(a+1); answer=a-b; sign="−"; }
        else if ("MUL".equals(op)) { a=rnd.nextInt(Math.min(12,3+level))+1; b=rnd.nextInt(10)+1; answer=a*b; sign="×"; }
        else if ("DIV".equals(op)) { b=rnd.nextInt(9)+1; answer=rnd.nextInt(10)+1; a=b*answer; sign="÷"; }
        else { a=rnd.nextInt(15+level*2)+1; b=rnd.nextInt(15+level*2)+1; answer=a+b; sign="+"; }
        text(a + " " + sign + " " + b + " = ?",38,true);
        List<Integer> choices = new ArrayList<>(); choices.add(answer);
        while (choices.size()<4) { int c=Math.max(0,answer+rnd.nextInt(13)-6); if(!choices.contains(c)) choices.add(c); }
        Collections.shuffle(choices); int[] colors={PURPLE,BLUE,PINK,GREEN};
        for(int i=0;i<4;i++){ final int c=choices.get(i); Button btt=button(String.valueOf(c),colors[i]); btt.setOnClickListener(v -> choose(c)); }
        button("Quitter",Color.DKGRAY).setOnClickListener(v -> confirmQuit());
    }

    private void choose(int value) {
        q++; total++;
        if(value==answer){ roundCorrect++; correct++; stars+=2; Toast.makeText(this,"Bravo " + name + " ! ⭐ +2",Toast.LENGTH_SHORT).show(); }
        else Toast.makeText(this,"La bonne réponse était " + answer,Toast.LENGTH_SHORT).show();
        save(); next();
    }

    private void finishRound() {
        int bonus=0; if(roundCorrect>=8){ level=Math.min(50,level+1); stars+=10; bonus=10; } save();
        screen(); text("🏆 Bravo " + name + " !",29,true); text(roundCorrect + " / 10 bonnes réponses",25,true); text("Niveau : " + level + " / 50",18,false); text("Étoiles : " + stars + (bonus>0 ? "  •  Bonus +10" : ""),18,false);
        button("▶ Continuer",PURPLE).setOnClickListener(v -> menu()); button("🔁 Rejouer",GREEN).setOnClickListener(v -> start(mode)); button("🏠 Accueil",Color.DKGRAY).setOnClickListener(v -> home());
    }

    private void trophies() {
        screen(); text("🏆 Trophées de " + name,28,true);
        text(correct>=5 ? "🥉 Premier pas — débloqué" : "🔒 Premier pas — 5 bonnes réponses",17,false);
        text(correct>=25 ? "🥈 Calculateur — débloqué" : "🔒 Calculateur — 25 bonnes réponses",17,false);
        text(correct>=50 ? "🥇 Champion — débloqué" : "🔒 Champion — 50 bonnes réponses",17,false);
        text(level>=10 ? "🚀 Explorateur — débloqué" : "🔒 Explorateur — niveau 10",17,false);
        button("← Retour",PURPLE).setOnClickListener(v -> home());
    }

    private void progress() {
        screen(); text("📊 Progression de " + name,28,true); int rate=total==0?0:(100*correct/total);
        text("Calculs réalisés : " + total,18,false); text("Bonnes réponses : " + correct,18,false); text("Réussite : " + rate + " %",18,false); text("Niveau : " + level + " / 50",18,false); text("Étoiles : " + stars,18,false);
        button("← Retour",PURPLE).setOnClickListener(v -> home());
    }

    private void profile() {
        screen(); text("👤 Mon profil",28,true); EditText e=new EditText(this); e.setText(name); e.setHint("Prénom / nom"); e.setTextSize(18); root.addView(e,new LinearLayout.LayoutParams(-1,dp(60)));
        button("💾 Enregistrer",GREEN).setOnClickListener(v -> { String n=e.getText().toString().trim(); if(n.isEmpty()){Toast.makeText(this,"Le nom est obligatoire",Toast.LENGTH_SHORT).show();return;} name=n; save(); home(); });
        button("Réinitialiser",PINK).setOnClickListener(v -> new AlertDialog.Builder(this).setTitle("Réinitialiser ?").setMessage("La progression sera remise à zéro.").setNegativeButton("Annuler",null).setPositiveButton("Oui",(d,w)->{level=1;stars=0;total=0;correct=0;save();home();}).show());
        button("← Retour",Color.DKGRAY).setOnClickListener(v -> home());
    }

    private void confirmQuit(){ new AlertDialog.Builder(this).setTitle("Quitter la partie ?").setMessage("Ta progression sera conservée.").setNegativeButton("Continuer",null).setPositiveButton("Quitter",(d,w)->home()).show(); }
    private void save(){ prefs.edit().putString("name",name).putInt("level",level).putInt("stars",stars).putInt("total",total).putInt("correct",correct).apply(); }
    private int dp(int v){ return (int)(v*getResources().getDisplayMetrics().density+0.5f); }
    @Override public void onBackPressed(){
        if(atNameEntry){
            new AlertDialog.Builder(this).setTitle("Quitter TafCalcul ?").setMessage("Veux-tu fermer l'application ?").setNegativeButton("Non",null).setPositiveButton("Oui",(d,w)->finish()).show();
        } else {
            confirmQuit();
        }
    }
}

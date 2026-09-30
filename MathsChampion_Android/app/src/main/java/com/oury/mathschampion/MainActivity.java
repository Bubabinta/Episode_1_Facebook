package com.oury.mathschampion;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
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

    private static final int BLUE = Color.rgb(20,118,255);
    private static final int CYAN = Color.rgb(30,196,255);
    private static final int PURPLE = Color.rgb(119,55,245);
    private static final int DEEP = Color.rgb(20,38,160);
    private static final int YELLOW = Color.rgb(255,190,25);
    private static final int ORANGE = Color.rgb(255,132,16);
    private static final int GREEN = Color.rgb(35,205,90);
    private static final int PINK = Color.rgb(205,78,255);
    private static final int WHITE = Color.WHITE;

    @Override public void onCreate(Bundle b) {
        super.onCreate(b);
        prefs = getSharedPreferences("tafcalcul", MODE_PRIVATE);
        level = prefs.getInt("level", 1);
        stars = prefs.getInt("stars", 0);
        total = prefs.getInt("total", 0);
        correct = prefs.getInt("correct", 0);
        nameEntry();
    }

    private GradientDrawable bg(int color, float radius) {
        GradientDrawable g = new GradientDrawable();
        g.setColor(color); g.setCornerRadius(dp((int)radius)); return g;
    }

    private GradientDrawable bg(int[] colors, float radius) {
        GradientDrawable g = new GradientDrawable(GradientDrawable.Orientation.TL_BR, colors);
        g.setCornerRadius(dp((int)radius)); return g;
    }

    private void screen() {
        ScrollView s = new ScrollView(this); s.setFillViewport(true);
        s.setBackground(bg(new int[]{Color.rgb(8,176,245), Color.rgb(25,86,245), Color.rgb(117,32,242)},0));
        root = new LinearLayout(this); root.setOrientation(LinearLayout.VERTICAL); root.setGravity(Gravity.CENTER_HORIZONTAL);
        root.setPadding(dp(18),dp(26),dp(18),dp(32)); s.addView(root); setContentView(s);
    }

    private void spacer(int h) { View v=new View(this); root.addView(v,new LinearLayout.LayoutParams(1,dp(h))); }

    private TextView label(String value, int size, boolean bold, int color) {
        TextView t = new TextView(this); t.setText(value); t.setTextSize(size); t.setTextColor(color); t.setGravity(Gravity.CENTER);
        t.setPadding(dp(6),dp(7),dp(6),dp(7)); if(bold)t.setTypeface(Typeface.DEFAULT,Typeface.BOLD); root.addView(t); return t;
    }

    private void brand() {
        TextView t = new TextView(this); SpannableString s = new SpannableString("TafCalcul");
        s.setSpan(new ForegroundColorSpan(WHITE),0,3,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new ForegroundColorSpan(YELLOW),3,9,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        t.setText(s); t.setTextSize(38); t.setTypeface(Typeface.DEFAULT,Typeface.BOLD); t.setGravity(Gravity.CENTER);
        t.setShadowLayer(8,0,4,Color.rgb(35,35,150)); root.addView(t,new LinearLayout.LayoutParams(-1,dp(58)));
        label("Jeu de calcul mental pour enfants",15,true,WHITE);
    }

    private TextView mascot() {
        TextView m = new TextView(this); m.setText("✦  🧮  ✦"); m.setTextSize(64); m.setGravity(Gravity.CENTER); m.setTextColor(WHITE);
        root.addView(m,new LinearLayout.LayoutParams(-1,dp(110))); return m;
    }

    private LinearLayout card(int color) {
        LinearLayout c=new LinearLayout(this); c.setOrientation(LinearLayout.VERTICAL); c.setGravity(Gravity.CENTER); c.setPadding(dp(18),dp(15),dp(18),dp(15)); c.setBackground(bg(color,26));
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,-2); lp.setMargins(0,dp(8),0,dp(8)); root.addView(c,lp); return c;
    }

    private TextView ctext(LinearLayout c,String txt,int size,boolean bold,int color){
        TextView t=new TextView(this);t.setText(txt);t.setTextSize(size);t.setGravity(Gravity.CENTER);t.setTextColor(color);if(bold)t.setTypeface(Typeface.DEFAULT,Typeface.BOLD);t.setPadding(4,dp(5),4,dp(5));c.addView(t);return t;
    }

    private Button button(String txt,int color){
        Button b=new Button(this);b.setText(txt);b.setTextSize(19);b.setAllCaps(false);b.setTextColor(WHITE);b.setTypeface(Typeface.DEFAULT,Typeface.BOLD);b.setBackgroundTintList(ColorStateList.valueOf(color));
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,dp(64));lp.setMargins(0,dp(7),0,dp(7));root.addView(b,lp);return b;
    }

    private Button cardButton(LinearLayout parent,String txt,int color){
        Button b=new Button(this);b.setText(txt);b.setTextSize(18);b.setAllCaps(false);b.setTextColor(WHITE);b.setTypeface(Typeface.DEFAULT,Typeface.BOLD);b.setBackgroundTintList(ColorStateList.valueOf(color));
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,dp(62));lp.setMargins(0,dp(6),0,dp(6));parent.addView(b,lp);return b;
    }

    private void nameEntry(){
        atNameEntry=true; screen(); brand(); mascot();
        LinearLayout c=card(Color.argb(238,245,250,255));
        ctext(c,"⭐  Entre ton prénom  ⭐",27,true,DEEP);
        ctext(c,"C’est toi le champion ! Dis-nous comment tu t’appelles pour commencer l’aventure.",16,false,Color.rgb(86,83,180));
        EditText e=new EditText(this);e.setHint("Prénom   Ex : Amadou");e.setTextSize(19);e.setSingleLine(true);e.setTextColor(DEEP);e.setHintTextColor(Color.rgb(150,145,215));e.setPadding(dp(18),0,dp(18),0);e.setBackground(bg(WHITE,24));
        LinearLayout.LayoutParams ep=new LinearLayout.LayoutParams(-1,dp(64));ep.setMargins(0,dp(16),0,dp(10));c.addView(e,ep);
        Button go=cardButton(c,"▶  Continuer  ❯",ORANGE);go.setOnClickListener(v->{String n=e.getText().toString().trim();if(n.isEmpty()){Toast.makeText(this,"Entre ton prénom pour continuer",Toast.LENGTH_SHORT).show();e.requestFocus();return;}name=n;save();home();});
        ctext(c,"🙂  Choisis ton prénom et deviens champion !",15,true,PURPLE);
    }

    private void home(){
        atNameEntry=false;screen();brand();mascot();
        LinearLayout hello=card(Color.argb(236,245,250,255));
        ctext(hello,"👑  Bonjour, " + name + " !",25,true,DEEP);
        ctext(hello,"Prêt à t’entraîner et à devenir un pro du calcul ?",16,false,Color.rgb(86,83,180));
        Button start=button("▶  Commencer",ORANGE);start.setOnClickListener(v->menu());
        LinearLayout row1=new LinearLayout(this);row1.setOrientation(LinearLayout.HORIZONTAL);root.addView(row1,new LinearLayout.LayoutParams(-1,-2));
        homeTile(row1,"➕➖\nChoisir une opération",Color.rgb(225,246,255),DEEP,v->menu());
        homeTile(row1,"🏆\nDéfi du jour",Color.rgb(246,230,255),DEEP,v->start("MIX"));
        LinearLayout row2=new LinearLayout(this);row2.setOrientation(LinearLayout.HORIZONTAL);root.addView(row2,new LinearLayout.LayoutParams(-1,-2));
        homeTile(row2,"📊\nProgression",Color.rgb(218,255,239),Color.rgb(20,99,74),v->progress());
        homeTile(row2,"⭐\nTrophées",Color.rgb(255,244,208),Color.rgb(140,75,20),v->trophies());
        button("👤  Profil",PURPLE).setOnClickListener(v->profile());
    }

    private void homeTile(LinearLayout row,String text,int color,int textColor,View.OnClickListener l){
        TextView t=new TextView(this);t.setText(text);t.setTextSize(18);t.setTextColor(textColor);t.setTypeface(Typeface.DEFAULT,Typeface.BOLD);t.setGravity(Gravity.CENTER);t.setPadding(dp(10),dp(15),dp(10),dp(15));t.setBackground(bg(color,24));t.setOnClickListener(l);
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(0,dp(122),1);lp.setMargins(dp(5),dp(6),dp(5),dp(6));row.addView(t,lp);
    }

    private void menu(){
        screen();brand();label("Choisis ton défi",27,true,WHITE);
        button("➕  Addition",BLUE).setOnClickListener(v->start("ADD"));
        button("➖  Soustraction",ORANGE).setOnClickListener(v->start("SUB"));
        button("✖  Multiplication",GREEN).setOnClickListener(v->start("MUL"));
        button("➗  Division",PURPLE).setOnClickListener(v->start("DIV"));
        button("🎲  Mélange",PINK).setOnClickListener(v->start("MIX"));
        button("←  Retour",Color.rgb(65,70,130)).setOnClickListener(v->home());
    }

    private void start(String m){mode=m;q=0;roundCorrect=0;next();}

    private void next(){
        if(q>=10){finishRound();return;} screen();brand();
        LinearLayout top=card(Color.argb(110,20,96,230));
        ctext(top,"Niveau " + level + "   ⭐ " + stars + "   •   " + (q+1) + " / 10",18,true,WHITE);
        mascot(); label("Bravo " + name + ", tu peux le faire !",20,true,WHITE);
        String op=mode;if("MIX".equals(op)){String[] ops={"ADD","SUB","MUL","DIV"};op=ops[rnd.nextInt(4)];}
        int a,b;String sign;
        if("SUB".equals(op)){a=rnd.nextInt(20+level)+1;b=rnd.nextInt(a+1);answer=a-b;sign="−";}
        else if("MUL".equals(op)){a=rnd.nextInt(Math.min(12,3+level))+1;b=rnd.nextInt(10)+1;answer=a*b;sign="×";}
        else if("DIV".equals(op)){b=rnd.nextInt(9)+1;answer=rnd.nextInt(10)+1;a=b*answer;sign="÷";}
        else{a=rnd.nextInt(15+level*2)+1;b=rnd.nextInt(15+level*2)+1;answer=a+b;sign="+";}
        LinearLayout qc=card(Color.argb(245,248,253,255));ctext(qc,a+"  "+sign+"  "+b+"  =  ?",38,true,DEEP);
        List<Integer> choices=new ArrayList<>();choices.add(answer);while(choices.size()<4){int x=Math.max(0,answer+rnd.nextInt(13)-6);if(!choices.contains(x))choices.add(x);}Collections.shuffle(choices);
        addAnswerRow(choices.get(0),BLUE,choices.get(1),ORANGE);addAnswerRow(choices.get(2),GREEN,choices.get(3),PURPLE);
        label("⭐ Progression : " + q + " / 10",16,true,WHITE);
        button("← Quitter la partie",Color.rgb(64,68,128)).setOnClickListener(v->confirmQuit());
    }

    private void addAnswerRow(int a,int ca,int b,int cb){
        LinearLayout row=new LinearLayout(this);row.setOrientation(LinearLayout.HORIZONTAL);root.addView(row,new LinearLayout.LayoutParams(-1,-2));
        answerButton(row,a,ca);answerButton(row,b,cb);
    }

    private void answerButton(LinearLayout row,int value,int color){
        Button b=new Button(this);b.setText(String.valueOf(value));b.setTextSize(31);b.setTextColor(WHITE);b.setTypeface(Typeface.DEFAULT,Typeface.BOLD);b.setBackgroundTintList(ColorStateList.valueOf(color));b.setOnClickListener(v->choose(value));
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(0,dp(86),1);lp.setMargins(dp(5),dp(6),dp(5),dp(6));row.addView(b,lp);
    }

    private void choose(int value){q++;total++;if(value==answer){roundCorrect++;correct++;stars+=2;Toast.makeText(this,"Bravo " + name + " ! ⭐ +2",Toast.LENGTH_SHORT).show();}else Toast.makeText(this,"La bonne réponse était " + answer,Toast.LENGTH_SHORT).show();save();next();}

    private void finishRound(){int bonus=0;if(roundCorrect>=8){level=Math.min(50,level+1);stars+=10;bonus=10;}save();screen();brand();mascot();LinearLayout c=card(Color.argb(240,248,252,255));ctext(c,"🏆 Bravo " + name + " !",28,true,DEEP);ctext(c,roundCorrect+" / 10 bonnes réponses",23,true,PURPLE);ctext(c,"Niveau : "+level+" / 50\nÉtoiles : "+stars+(bonus>0?"  •  Bonus +10":""),18,false,Color.rgb(70,75,145));button("▶ Continuer",ORANGE).setOnClickListener(v->menu());button("🔁 Rejouer",GREEN).setOnClickListener(v->start(mode));button("🏠 Accueil",PURPLE).setOnClickListener(v->home());}

    private void trophies(){screen();brand();mascot();LinearLayout c=card(Color.argb(242,255,249,225));ctext(c,"🏆 Trophées de " + name,27,true,Color.rgb(150,90,15));ctext(c,correct>=5?"🥉 Premier pas — débloqué":"🔒 Premier pas — 5 bonnes réponses",17,false,DEEP);ctext(c,correct>=25?"🥈 Calculateur — débloqué":"🔒 Calculateur — 25 bonnes réponses",17,false,DEEP);ctext(c,correct>=50?"🥇 Champion — débloqué":"🔒 Champion — 50 bonnes réponses",17,false,DEEP);ctext(c,level>=10?"🚀 Explorateur — débloqué":"🔒 Explorateur — niveau 10",17,false,DEEP);button("← Retour",PURPLE).setOnClickListener(v->home());}

    private void progress(){screen();brand();LinearLayout c=card(Color.argb(242,229,255,244));int rate=total==0?0:(100*correct/total);ctext(c,"📊 Progression de " + name,27,true,Color.rgb(20,105,74));ctext(c,"Calculs réalisés : " + total + "\nBonnes réponses : " + correct + "\nRéussite : " + rate + " %\nNiveau : " + level + " / 50\nÉtoiles : " + stars,19,false,DEEP);button("← Retour",PURPLE).setOnClickListener(v->home());}

    private void profile(){screen();brand();LinearLayout c=card(Color.argb(240,248,252,255));ctext(c,"👤 Mon profil",27,true,DEEP);EditText e=new EditText(this);e.setText(name);e.setTextSize(19);e.setTextColor(DEEP);e.setBackground(bg(WHITE,24));e.setPadding(dp(18),0,dp(18),0);c.addView(e,new LinearLayout.LayoutParams(-1,dp(64)));cardButton(c,"💾 Enregistrer",GREEN).setOnClickListener(v->{String n=e.getText().toString().trim();if(n.isEmpty()){Toast.makeText(this,"Le prénom est obligatoire",Toast.LENGTH_SHORT).show();return;}name=n;save();home();});button("Réinitialiser la progression",PINK).setOnClickListener(v->new AlertDialog.Builder(this).setTitle("Réinitialiser ?").setMessage("La progression sera remise à zéro.").setNegativeButton("Annuler",null).setPositiveButton("Oui",(d,w)->{level=1;stars=0;total=0;correct=0;save();home();}).show());button("← Retour",PURPLE).setOnClickListener(v->home());}

    private void confirmQuit(){new AlertDialog.Builder(this).setTitle("Quitter la partie ?").setMessage("Ta progression sera conservée.").setNegativeButton("Continuer",null).setPositiveButton("Quitter",(d,w)->home()).show();}
    private void save(){prefs.edit().putString("name",name).putInt("level",level).putInt("stars",stars).putInt("total",total).putInt("correct",correct).apply();}
    private int dp(int v){return (int)(v*getResources().getDisplayMetrics().density+0.5f);}
    @Override public void onBackPressed(){if(atNameEntry)new AlertDialog.Builder(this).setTitle("Quitter TafCalcul ?").setMessage("Veux-tu fermer l’application ?").setNegativeButton("Non",null).setPositiveButton("Oui",(d,w)->finish()).show();else confirmQuit();}
}

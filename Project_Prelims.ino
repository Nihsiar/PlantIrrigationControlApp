#include <LiquidCrystal.h>

LiquidCrystal lcd(12,11,5,4,3,2);
int soilSen, senValue, dryValue = 1023, wetValue = 150;
int led[] = {9,7};//6 for green, 7 for red
int waterPump = 6;

void setup() {
  // put your setup code here, to run once:
  for (int i=0;i<2;i++)
    pinMode(led[i], OUTPUT);
  pinMode(waterPump,OUTPUT);
  pinMode(A0,INPUT);//soil sensor
  Serial.begin(9600);
  lcd.begin(16,2);
}

void loop() {
  //digitalWrite(6,HIGH);
  // put your main code here, to run repeatedly:
  valueGathering();
  execution();
  
  //delay(500);
  //lcd.clear();
}

void valueGathering(){
  soilSen = analogRead(A0);
  senValue = map(soilSen,dryValue,wetValue,0,100);//dry value, wet value, 0,100
  ///*
  Serial.print("Raw ");
  Serial.print(soilSen);
  Serial.print("| Value ");
  Serial.print(senValue);
  Serial.println(" %");
  delay(500);
  //*/
  lcd.setCursor(1,0);
  lcd.print("Moisture: "); 
  lcd.print(senValue,DEC);
  lcd.print("%");
    
}

void execution(){
  //if the soil is dry
  if(senValue<30){
    digitalWrite(waterPump,HIGH);
    digitalWrite(9,HIGH);
    digitalWrite(7,LOW);
    lcd.setCursor(2,1);
    lcd.print("Status: Dry");
  }
  else if(senValue >= 60){
    lcd.setCursor(2,1);
    lcd.print("Status: Wet");
    digitalWrite(waterPump,LOW);
    digitalWrite(7,HIGH);
    digitalWrite(9,LOW);
    lcd.setCursor(2,1);
  }
}


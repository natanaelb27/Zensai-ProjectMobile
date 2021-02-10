<?php
header('content-type:application/json;charset=utf-8');
include "conn.php";

$z=mysqli_query($mysqli,"SELECT id_cust FROM mst_customer ORDER BY id_cust DESC LIMIT 1;");

if (mysqli_num_rows($z)>0)
{
   while($r=mysqli_fetch_array($z))
   {
   $lastid =$r["id_cust"];
   }
}

$digita = substr($lastid,9,1);

if($digita < 9){
   $digita = $digita + 1;
   $defaultcode = substr($lastid,0,9);
   $newid = $defaultcode . $digita;
   echo $newid;
   }
   else{
      $digitb = substr($lastid,8,2);
      if($digitb < 99){
         $digitb = $digitb + 1;
         $defaultcode = substr($lastid,0,8);
         $newid = $defaultcode . $digitb;
         echo $newid;
      }
      else{
      $digitc = substr($lastid,7,3);
      $digitc = $digitc + 1;
      $defaultcode = substr($lastid,0,7);
      $newid = $defaultcode . $digitc;
      echo $newid;
      }
   }
?>
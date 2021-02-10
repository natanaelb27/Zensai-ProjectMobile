<?php
   header('content-type:application/json;charset=utf-8');
   include "conn.php";

   $q=mysqli_query($mysqli,"SELECT tbl_cart.id_cart AS id_cart, mst_customer.username AS username, mst_menu.nama_menu AS nama_menu, tbl_cart.quantity AS quantity, tbl_cart.service AS service FROM tbl_cart JOIN mst_customer ON tbl_cart.id_cust = mst_customer.id_cust JOIN mst_menu ON tbl_cart.id_menu = mst_menu.id_menu WHERE tbl_cart.status = 'wait' ORDER BY tbl_cart.id_cart");
   $response=array();
   
   if (mysqli_num_rows($q)>0)
   {
     $response["data"]=array();
     while($r=mysqli_fetch_array($q))
     {
     $user=array();
      $user["id_cart"]=$r["id_cart"];
      $user["username"]=$r["username"];
      $user["nama_menu"]=$r["nama_menu"];
      $user["quantity"]=$r["quantity"];
      $user["service"]=$r["service"];
      array_push($response["data"],$user);     
     }

     $response["success"]=1;
     $response["message"]="data berhasil diambil";
      echo json_encode($response);
   }
   else
   { 
     $response["success"]=-1;
     $response["message"]="data kosong";
     echo json_encode($response);

   }
     
?> 
<?php
   header('content-type:application/json;charset=utf-8');
   include "conn.php";

   $id_cart = $_POST['id_cart'];
   $q=mysqli_query($mysqli,"SELECT tbl_cart.id_cart AS id_cart, mst_menu.nama_menu AS nama_menu, tbl_cart.quantity AS quantity, mst_menu.harga AS harga, mst_menu.harga * tbl_cart.quantity AS subtotal FROM mst_menu JOIN tbl_cart ON mst_menu.id_menu = tbl_cart.id_menu WHERE tbl_cart.id_pembayaran='$id_cart'");
   $response=array();
   if (mysqli_num_rows($q)>0)
    {
     $response["data"]=array();
     while($r=mysqli_fetch_array($q))
     {
     $user=array();
     $user["id_cart"]=$r["id_cart"];
      $user["nama_menu"]=$r["nama_menu"];
      $user["quantity"]=$r["quantity"];
      $user["harga"]=$r["harga"];
      $user["subtotal"]=$r["subtotal"];
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
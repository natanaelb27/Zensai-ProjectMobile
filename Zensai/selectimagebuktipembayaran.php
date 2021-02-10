<?php
   header('content-type:application/json;charset=utf-8');
   include "conn.php";

   $id_pembayaran = $_POST['id_pembayaran'];

   $q=mysqli_query($mysqli,"SELECT bukti_pembayaran FROM tbl_pembayaran WHERE id_pembayaran='$id_pembayaran'");
   $response=array();
   
   if (mysqli_num_rows($q)>0)
   {
     $response["data"]=array();
     while($r=mysqli_fetch_array($q))
     {
     $user=array();
      $user["buktipembayaran"]=$r["bukti_pembayaran"];
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
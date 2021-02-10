<?php
   header('content-type:application/json;charset=utf-8');
   include "conn.php";

   $username = $_POST['username'];
   $qcust=mysqli_query($mysqli,"SELECT id_cust FROM mst_customer WHERE username = '$username'");
   if(mysqli_num_rows($qcust)>0){
    while($r = mysqli_fetch_array($qcust)){   
      $id_cust = $r['id_cust'];
    }
  }

   $q=mysqli_query($mysqli,"SELECT mst_iklan.id_iklan AS id_iklan, tbl_pembayaran.tanggal_pembayaran AS tanggal_pembayaran, tbl_pembayaran.total_pembayaran AS total_pembayaran FROM tbl_pembayaran JOIN mst_iklan ON tbl_pembayaran.id_pembayaran = mst_iklan.id_pembayaran WHERE mst_iklan.id_cust='$id_cust' AND (mst_iklan.status='wait' OR mst_iklan.status='refused')");
   $response=array();
   
   if (mysqli_num_rows($q)>0)
   {
     $response["data"]=array();
     while($r=mysqli_fetch_array($q))
     {
     $user=array();
      $user["id_iklan"]=$r["id_iklan"];
      $user["tglpembayaran"]=$r["tanggal_pembayaran"];
      $user["totalpembayaran"]=$r["total_pembayaran"];
      
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
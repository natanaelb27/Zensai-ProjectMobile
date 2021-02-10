<?php
   header('content-type:application/json;charset=utf-8');
   include "conn.php";

   $nama_menu = $_POST['nama_menu'];
    $username = $_POST['username'];

    $qcust=mysqli_query($mysqli,"SELECT id_cust FROM mst_customer WHERE username = '$username'");
    if(mysqli_num_rows($qcust)>0){
      while($r = mysqli_fetch_array($qcust)){   
        $id_cust = $r['id_cust'];
      }
    }


    $qmenu=mysqli_query($mysqli,"SELECT id_menu FROM mst_menu WHERE nama_menu = '$nama_menu'");
    if(mysqli_num_rows($qmenu)>0){
      while($r = mysqli_fetch_array($qmenu)){   
        $id_menu = $r['id_menu'];
      }
    }

    $qcart=mysqli_query($mysqli,"SELECT * FROM tbl_cart WHERE id_cust = '$id_cust' AND id_menu = '$id_menu' AND status = 'pending'");
    if(mysqli_num_rows($qcart)>0){
      while($r = mysqli_fetch_array($qcart)){   
        $quantity = $r['quantity'];
      }
      $qupdate=mysqli_query($mysqli,"UPDATE tbl_cart SET quantity = $quantity - 1 WHERE id_cust = '$id_cust' AND id_menu = '$id_menu' AND status = 'pending'");
      if($r['quantity'] < 1){
        $qdelete=mysqli_query($mysqli,"DELETE FROM tbl_cart WHERE id_cust = '$id_cust' AND id_menu = '$id_menu' AND status = 'pending' AND quantity < 1");
      }
      $response = array();

      if($qupdate){
        $response["success"] =1;
        $response["message"] = "Data berhasil ditambah";
        echo json_encode($response);
      }
      else{
        $response["success"] =0;
        $response["message"] = "Data gagal ditambah";
        echo json_encode($response);
      }
      
    } 


	  
?>	
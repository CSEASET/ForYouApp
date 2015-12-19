<?php 

header("Content-type: text/json");
echo "[";
if ($handle = opendir('pdf/papers')) {
    while (false !== ($entry = readdir($handle))) {
        if ($entry != "." && $entry != "..") {
            // do here
			echo file_get_contents("pdf/papers/$entry") . ",";
//if)
//echo ",";
//else
//break ;
        }
    }
    closedir($handle);
} 
echo "]";


?>
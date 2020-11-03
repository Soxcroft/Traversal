cd src
for i in {15..17}
do
  java SU23688963 ../samples2/board_$i.txt ../samples2/moves_$i.txt | diff -s - ../samples2/output_$i.txt
done
cd ..

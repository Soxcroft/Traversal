cd src
for i in {1..9}
do
  java SU23688963 ../samples1/board_0$i.txt ../samples1/moves_0$i.txt | diff -s - ../samples1/output_0$i.txt
done
for i in {10..14}
do
  java SU23688963 ../samples1/board_$i.txt ../samples1/moves_$i.txt | diff -s - ../samples1/output_$i.txt
done
cd ..

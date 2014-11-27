/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robovikings.competition.frc;

/**
 *
 * @author frcdev
 */
public class ControlScheme {
 /*ANTHONY:
  * Controls:
  * Left Trigger(hold)      - charge shooter
  * Left Bumper(toggle)     - pan camera
  * Right Trigger(tap)      - shoot
  * XBOX(toggle w/ pilot)   - fall
  * Select(toggle)          - climb
  * Y(tap)                  - move hooks to top
  * A(tap)                  - move hooks to bottom
  * Right Click(toggle)     - fangs
  * Left Click(toggle)      - dump
  * LIST CONTROLS ON SMART DASHBOARD -iffy
  * ADD TIME TO SMART DASHBOARD
  * POSIBLE SOUNDS
  * 
  * *CONOR:
  * Controls:
  * XBOX(toggle w/ copilot) - fall
  * Left Stick              - drive
  * Right Stick             - turn
  * Left Press              - shift
  * Y(tap)                  - retract big piston
  * X(tap)                  - full extend big piston
  * A(tap)                  - half extend big piston
                  ################                                ################                    
                #####t;ii,######                                ##########Liii;,###########         
                ####Gi;ii;t#####                                ##########Liiii,###########         
                ####Li;i;,j#####                                ##########Li;ii,###########         
                ####fi;;;,f#####                                ##########Li;ii,###########         
                ####j;;;;,f#####                                ##########Giiii,###########         
                ###Gj;;;;,jit##########################################GDWGtiii,i##########         
                ##fGt;;i;,Eiii##################WKKEK#################fjjWLtiit;;i#########         
         ########jjKt;;;,,iiiii################KKKKEG################jjjjtLti;;;,iiE#######         
         #######jjtiiii;;;;,;;;;;################KKWK##############jjjjtftiii,;i;;i;i######         
         ######jtttiiii;;;;,;j;,;;;############KKEKEGK###########jjjjjtfLiiii;;;;;;;;i#####         
         ####jttttiiiiiii;;,,E,,,;:;;;i######GKKKKKKDDtK####EttjjjjjjjtfLjttiiii;,,,,;;####         
         ###jjttjjjjtttiii;,,;,,,,,,;;,;;;;iitKKKKKKEEEtittttttjjjjjjttjfjtitiiiii;;:,,,;##         
         ##jjjjfjjjjjjjjjji.,,,,,,,,,;;;;;;iiiiiiiiiiiiiiiitttttjjjjjjjLttjjjjjtii;;;;;,,##         
         ##fffffLLGtiiii;;;,tt::,,,,,,;;;;;;;;;iiiiiiiiiiiiittttjjjjjjfftii;;;;,,,tti;;;;K#         
         #LffLGttttttiiii;;,,,,i,,:,,,,;;;;;;;ti;,i,;t,iiiiiitttjjjjfjjjttiii;;;,,,,,,ti;i#         
         #LLjjjjjjjttttttti;;,,::;:.:,:,;;;;;;;;;;;;iitiiiiiittjjjfffjjjjtjjjtii;;;;,,,,,ij         
         Lfffjjjjffffffjjfjjji,,:,t;,,;;;;;i;;i;iii;iiiiiiitttjjjLffffffffffffjjjjji;,,,,,,#        
        #fffffffffffffffffjjjfj;,,,,t;;;;iiiiiiiiiiiiiiittttttjLjfffffffffffffffjjjjttii;,,#        
        Kfffffffffffffffffjfjjjjt,,,,;,jtiiiiiiiiiiiiiiiittjftjjjffffffffffffffjtttttttii;,,        
       #fffffffffffffffffffffjjffj;;,;;;;;;iiiiiiiiiiiiiiittttjjffffffffffffffjDDEjtttti;;;;#       
       #fffffffffjttttjjfffffffffffj;;;;iiiiiiiiiiiiiiiiiittttjjffffffffffLLGELLGGLjtjttii;,;       
       Lffffffjjtttii;;,;jfffffffffjjjtiiiiiiiiiiiiiitiittttjjffffffffffLLLGGDDLGfGEtjttii;;;#      
      #fffffffjjjttii;;,,,;jfffffjfffffjjjjjjjjjjjjjjjjjjjjfjfffffffffffLLGGEDEDGjGKtjjtti;;,#      
      Dfffffffjjjjtti;;;,:::jfffffffffffjjffjfjfjjjffjjfffffffffffffffffLGGGEDDDDfDfjjjtti;;,;      
     #fffffffffjjjjffji;;,:::ffffffffffjfffjjfjjfKWLLjjLjfffffffffffffLLLGDDGWEDDDWjfjjti;;;;;#     
     #fjjfjfffffKWWWWW#Wt;,::;ffffjffjjfjffjjfKWKKEfWWfjjffffffffffffffffLGDGGW#WWfffjjGKf;;;;t     
     LfjffjffffWKWWWKWWWWW;,,,jffffffLffjfffjWEWGKLEK;WLffffjGjfffffLLKKKWLLLGLLLfffLKEEDDE;;;;     
    #fjjffjjffWWEEEEDGGKWWK,,,iffffLtffffffffWGGKEEE;,,WfffffjjiffLGGKEEKKKffLLLfLLGWKEDDDEii;;#    
    #ffjffjjff#KKKKKKEDGDWWi;,;fffffffji,;ffWLfLDEELGGGWffGfti,ffLGGKWEDEKWGfLLLLLGDKKKKEGKii;;;#   
    LfjfjjjjjLKKKKKEKKKEGKWE;;;ffffLGLfLt;jf#ffLKKEEDGGWLLLLLttELGGGKKKKEKWffLLLLGDDKWKKKE#tii;;#   
   #fjjjffjjjGKKKKKKKKKKEG#E;;;ffffLGfjGjiffWLfEELDDEDDWLGGLffjfLGGGKWEKKE#ffLLLLGDDWWKKEWttii;;#   
   #fjjjfffjjEKKKKKKKKWKKDWDi;tjffffLfLffjjff#DfLLLEEKWWfLLLLLffLLGDG#KWW#ffLLfffLGDD####jttii;;G   
   LfjjjfffjjEKKKKKKKKKKKEWjiijjfffffffffffffE#LLLLED#WLffffffLfLLLLLLLLLLfLLLKWEffLLLLfjjttiii;i   
  #ffjjjjjffjDKKKKKKWWKWKEDtijjfffffffffjffjfffW#W#WWLffffffffffLLLLLLLLfLGGWKEEDEffffffjjtttiiii#  
  #fjjjjjffffjKKKKKKKKWKKEtttjfjjttttttjjjfjjfffffffffffffffffffffLLLLLLLGGDKKEDEKKjfffjjjjttii;;#  
  #fjjjjffffffDKKKKKKKKKKDjffjjttii;;;,,,tjjfffffffffffffffffjjjjjtjjfLLGGDEKKKKEKWjfffjjjtttiiii#  
  GfjjjjjffffffEKKKWKKKKEfffjjjjjjjjjjt,,,;jfjffffffffffLLLfjjtttti;iijLGDDGWKKEEKLffffjjjjttiiiif  
  LfjjjjjjjfjffffKKKKKEfffffjffWWWWWWWDjt,,,jfjfffffffLLLLffjjttiiii;;;tLGDDDWWWWWffffffjjjtttiiii  
  ffjjjjjjjffffjfffffffffffffWWKEDDDGGDWDji,;ffffffffLLLLLfffjjjjtii;;,;jGGGGGGLLfLffffjjjjtttiiii  
  fjjjjjjjffjjffffffffffffffWWEKEEEEGEDLDWji,ifffLLLLLLLLLffWWWWWW#K;,,,ijLLLLLfLLffffffjjjtttiiii  
 #fjjjjjjfjffjfjffffffffjffKWEEKEEEEEKEE;DWj,ifLLLLLLfLLLLfKWWWWKKW##i,;;tfLLLLLLLLfffffjjjjtttiii  
 #fjjjjjjjjjffffffffffffjfLWEEEKEEEEEDEEEjWftifLLLLLLLffffWKWWWEDKKK##i;;ijLLLLLLLLfLfffjjjjttiiii  
 #fjjjjjjjjjjjjfffffffffjfKKEEKKEEEEEELKKEDKjiLLLLLLLffffWKKEKEEDGGGW#G;;ijLLLLLLLLfLffffjjjtttiii  
 #fjjjjjjjjjjjffffffffffjfWEEEEEEEEEEEEEKEGKftGLLLLLLffff#KKKKKKKKEDG#KiiifLLLLLLLLLfffffjjjtttiit# 
 #fjjjjjjjjjjfffffffffffjfKEEEEEEEEEEEEEKEDWfjGLLLLLLffff#KKKKKWKWKKDGEiitLLLLLLLLLLLfffffjjttttii  
 #fjjjjjjjjjffjfffffffffffDDEEEEEEEEEEKEKKDKfLLLLLLLLLfffEKKKKKWWKWKKDGitfLLLLLLLLLLLfffffjjjttiii  
  fjjjjjjjjjjfjffffffffffjLKEEEEEEEEEKEEEEDLfLLLLLLLLLLLfEKKKKWWWWWKKEjtfLLLLLLLLLLLLLffffjjjttitt  
  fjjjjjjjjjjfjjfffffffffffGEEEKEEEEEEKKKEKffLLLLLLLLLLLLDKWKKKKWWWKKEjLLLLLLLLLLLLLLLfffjfjjttttL  
  DjjjjjjjjjjfjfffffffffffffGEEEKEKKKEKEEEfffffLLLLLLLLLLLKKKKKKWWWWKELLLLLLLLLLLLLLLLLffffjjtttt#  
  #jjjjjjjjjjjjfjffffffffffffLEEKKKKKDKELffLLLLLGLLLGGLGGLKKKKKKKWWKKLLLLLLLLLLLLLLLLLLLfffjjtttt#  
  #fjjjjjjjjjjfffffffffffffffffLGEKKKDfjj#                #KKKKKKWKKLLLLLLLLLLLLLLLLLLLLLfffjjttj   
   LjjjjjjjjfffffffffffffffffffGffffjjK#                    #KKKKKW#DGLLGLLLLLLLLLLLLLLLLffffjjjW   
   #fjjjjjjfffffffffffffffffL##                                #     ##DGGGGLGGGGGGLLLLLLLfffjjj#   
    GjjjjjffffffffffffffffD#                                            #EGLGGGGGGGGGLLLLLffffj#    
    #fjjjfffffffffffffffE#                                                #EGGGGGGGGGGLLLLLfffG     
     #fffffffffLfLfffLE#                                                    #KDGGGGGGGGGGLLLfL#     
      #LffffLLLLLLLL##                                                        ##DGGGGGGGGGLLE#      
       ##LLLLLLLLW#                                                              ##EDDDGGGE#        
          ######                                                                    ######          
                                                                                             
  */   
}

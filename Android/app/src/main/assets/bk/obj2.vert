attribute vec3 vPosition;
attribute vec2 vCoord;
uniform mat4 vMatrix;
uniform vec3 vKa;
uniform vec3 vKd;
uniform vec3 vKs;

varying vec2 textureCoordinate;

attribute vec3 vNormal;         //娉曞悜閲�
varying vec4 vDiffuse;          //鐢ㄤ簬浼犻�掔粰鐗囧厓鐫�鑹插櫒鐨勬暎灏勫厜鏈�缁堝己搴�
varying vec4 vAmbient;          //鐢ㄤ簬浼犻�掔粰鐗囧厓鐫�鑹插櫒鐨勭幆澧冨厜鏈�缁堝己搴�
varying vec4 vSpecular;          //鐢ㄤ簬浼犻�掔粰鐗囧厓鐫�鑹插櫒鐨勯暅闈㈠厜鏈�缁堝己搴�

//瀹氫綅鍏夊厜鐓ц绠楃殑鏂规硶
void calculateLight(             //瀹氫綅鍏夊厜鐓ц绠楃殑鏂规硶
  vec3 normal,               //娉曞悜閲�
  vec4 vA,
  vec4 vD,
  vec4 vS,
  vec3 camera,
  vec3 lightLocation,        //鍏夋簮浣嶇疆
  vec4 lightAmbient,         //鐜鍏夊己搴�
  vec4 lightDiffuse,         //鏁ｅ皠鍏夊己搴�
  vec4 lightSpecular         //闀滈潰鍏夊己搴�
){
  vA=lightAmbient;         //鐩存帴寰楀嚭鐜鍏夌殑鏈�缁堝己搴�
  vec3 normalTarget=vPosition+normal;   //璁＄畻鍙樻崲鍚庣殑娉曞悜閲�
  vec3 newNormal=(vMatrix*vec4(normalTarget,1)).xyz-(vMatrix*vec4(vPosition,1)).xyz;
  newNormal=normalize(newNormal);   //瀵规硶鍚戦噺瑙勬牸鍖�
  //璁＄畻浠庤〃闈㈢偣鍒版憚鍍忔満鐨勫悜閲�
  vec3 eye= normalize(camera-(vMatrix*vec4(vPosition,1)).xyz);
  //璁＄畻浠庤〃闈㈢偣鍒板厜婧愪綅缃殑鍚戦噺vp
  vec3 vp= normalize(lightLocation-(vMatrix*vec4(vPosition,1)).xyz);
  vp=normalize(vp);//鏍煎紡鍖杤p
  vec3 halfVector=normalize(vp+eye);    //姹傝绾夸笌鍏夌嚎鐨勫崐鍚戦噺
  float shininess=50.0;             //绮楃硻搴︼紝瓒婂皬瓒婂厜婊�
  float nDotViewPosition=max(0.0,dot(newNormal,vp));    //姹傛硶鍚戦噺涓巚p鐨勭偣绉笌0鐨勬渶澶у��
  vD=lightDiffuse*nDotViewPosition;                //璁＄畻鏁ｅ皠鍏夌殑鏈�缁堝己搴�
  float nDotViewHalfVector=dot(newNormal,halfVector);   //娉曠嚎涓庡崐鍚戦噺鐨勭偣绉�
  float powerFactor=max(0.0,pow(nDotViewHalfVector,shininess));     //闀滈潰鍙嶅皠鍏夊己搴﹀洜瀛�
  vS=lightSpecular*powerFactor;               //璁＄畻闀滈潰鍏夌殑鏈�缁堝己搴�
}

void main(){
    gl_Position = vMatrix*vec4(vPosition,1);
    textureCoordinate = vCoord;

    vec3 lightLocation=vec3(0.0,-30.0,-30.0);      //鍏夌収浣嶇疆
    vec3 camera=vec3(0,1000.0,0);
    float shininess=10.0;             //绮楃硻搴︼紝瓒婂皬瓒婂厜婊�

     vec3 newNormal=normalize((vMatrix*vec4(vNormal+vPosition,1)).xyz-(vMatrix*vec4(vPosition,1)).xyz);
     vec3 vp=normalize(lightLocation-(vMatrix*vec4(vPosition,1)).xyz);
     vDiffuse=vec4(vKd,1.0)*max(0.0,dot(newNormal,vp));                //璁＄畻鏁ｅ皠鍏夌殑鏈�缁堝己搴�

     vec3 eye= normalize(camera-(vMatrix*vec4(vPosition,1)).xyz);
     vec3 halfVector=normalize(vp+eye);    //姹傝绾夸笌鍏夌嚎鐨勫崐鍚戦噺
     float nDotViewHalfVector=dot(newNormal,halfVector);   //娉曠嚎涓庡崐鍚戦噺鐨勭偣绉�
     float powerFactor=max(0.0,pow(nDotViewHalfVector,shininess));     //闀滈潰鍙嶅皠鍏夊己搴﹀洜瀛�
     vSpecular=vec4(vKs,1.0)*0.2;               //璁＄畻闀滈潰鍏夌殑鏈�缁堝己搴�

     vAmbient=vec4(vKa,1.0);
}
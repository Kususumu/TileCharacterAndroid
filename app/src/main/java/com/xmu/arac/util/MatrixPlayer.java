package com.xmu.arac.util;


import com.xmu.arac.draw.Vector3;
import com.xmu.arac.draw.Vertex3D;

//aij , i is row , j is line
//Some time the coords transfrom need Matrix Calculating
public class MatrixPlayer {
    double[][] matrix4=new double [4][4];
    int n=4;

    public static double[][] setInverseRotateMartix(float[] rMatrix){
        double[][] inverseRotateMatrix = MatrixPlayer.reverseMatrix(MatrixPlayer.getTransformMatrix(rMatrix));
        return inverseRotateMatrix;
    }

    //Usage to get the inverse matrix of the base
    static double[][] setInverseMvpMartix(float[] mvpMatrix){
        double[][] inverseMvpMatrix = MatrixPlayer.reverseMatrix(MatrixPlayer.getTransformMatrix(mvpMatrix));
        return inverseMvpMatrix;
    }


    double[][] setInverseMMartix(float[] modelMatrix){
        double[][] inverseMMatrix = MatrixPlayer.reverseMatrix(MatrixPlayer.getTransformMatrix(modelMatrix));
        return inverseMMatrix;
    }

    double[][] setInversePMartix(float[] floorProjectionMatrix){
        double[][] inversePMatrix = MatrixPlayer.reverseMatrix(MatrixPlayer.getTransformMatrix(floorProjectionMatrix));
        return inversePMatrix;
    }

    //Get the Inverse Base
    public static Vertex3D getInverseVertex(double[][] t, Vertex3D v){
        return MatrixPlayer.multiplyVertexWithMatrix(v ,t);
    }

    //Get the Inverse Ray
    public static Vector3 getInverseVector(double[][] t, Vector3 v){
        return MatrixPlayer.multiplyVectorWithMatrix(v ,t);
    }

    //In Float R,Out matrix4(double[][])t
    public static double[][] getTransformMatrix(float[] R)
    {
        double[][] t = new double[4][4];

        for(int i = 0 ; i < 13 ; i=i+4)
        {
            t[ i/4 ][ 0 ] = R[i];
            t[ i/4 ][ 1 ] = R[i+1];
            t[ i/4 ][ 2 ] = R[i+2];
            t[ i/4 ][ 3 ] = R[i+3];
        }
        return t;
    }

    static public Vector3 multiplyVectorWithMatrix(Vector3 v, double[][] m)
    {
        Vector3 t = new Vector3(0,0,0);
        t.x=v.x* m[0][0]+v.y* m[1][0]+v.z* m[2][0]+1*m[3][0];
        t.y=v.x* m[0][1]+v.y* m[1][1]+v.z* m[2][1]+1*m[3][1];
        t.z=v.x* m[0][2]+v.y* m[1][2]+v.z* m[2][2]+1*m[3][2];
        return t;
    }

    //Can make (a Point or a Vector) multiply with a Matrix
    //Matrix is Left,Vertex is Right
    static public Vertex3D multiplyVertexWithMatrix(Vertex3D v, double[][] m)
    {
        Vertex3D t = new Vertex3D(0,0,0);
        t.x=v.x* m[0][0]+v.y* m[1][0]+v.z* m[2][0]+1* m[3][0];
        t.y=v.x* m[0][1]+v.y* m[1][1]+v.z* m[2][1]+1* m[3][1];
        t.z=v.x* m[0][2]+v.y* m[1][2]+v.z* m[2][2]+1* m[3][2];
        return t;
    }

    //Matrix Multiply
    static public double[][] multiplyMatrix(double[][] m,double[][] m1)
    {
        //Judge if the matrix A can multiply matrix B
        if(m[0].length != m1.length)
            return null;

        int h = m.length;
        int v = m1[0].length;

        double t[][]=new double[h][v];

        for(int i=0;i<h;i++)
        {
            for(int j=0;j<v;j++)
            {
                for(int k=0;k<m1.length;k++)
                {
                    t[i][j] +=m[i][k] * m1[k][j];
                }
            }
        }

        return t;
    }

    //Get Reverse Matrix (Ni Matrix)
    //Need the Confactor
    //Need the Value(Mo(ģ))
    //Need the Trans
    static public double[][] reverseMatrix(double[][] m)
    {
        double[][] t = new double[m.length][m[0].length];

        double A = valueMatrix(m);
        //System.out.println(A);

        for(int i =0;i<m.length;i++)
        {
            for(int j=0;j<m[0].length;j++)
            {
                if((i+j)%2==0)
                {
                    t[i][j] = valueMatrix(confactorMatrix(m,i+1,j+1))/A;
                }
                else
                {
                    t[i][j]= -valueMatrix(confactorMatrix(m,i+1,j+1))/A;
                }
            }
        }

        t= transMatrix(t);

        return t;
    }

    //transMatrix
    static public double[][] transMatrix(double[][] m)
    {
        double[][] t=new double[m[0].length][m.length];
        for(int i=0;i<m.length;i++)
        {
            for(int j=0;j<m[0].length;j++)
                t[j][i] = m[i][j];
        }
        return t;
    }

    //Need the Value(Mo(ģ))
    static public double valueMatrix(double[][] m)
    {
        if(m.length == 2) {
            return m[0][0]*m[1][1] - m[0][1]*m[1][0];
        }

        double result = 0;
        int num = m.length;
        double[] nums=new double[num];
        for(int i=0;i<m.length;i++)
        {
            if(i%2 == 0)
            {
                //Debug
                nums[i]= m[0][i]*valueMatrix(confactorMatrix(m,1,i+1));
            }

            else
            {
                nums[i]=-m[0][i]*valueMatrix(confactorMatrix(m,1,i+1));
            }
        }

        for(int i=0;i<m.length;i++)
        {
            result +=nums[i];
        }

        return result;
    }

    //get the Confactor
    //h,v is coords
    static public double[][] confactorMatrix(double[][] m,int h,int v)
    {
        int H=m.length;
        int V=m[0].length;
        double[][] t = new double[H-1][V-1];

        for(int i=0;i<t.length;i++)
        {
            if(i<h-1)
            {
                for(int j=0;j<t[i].length;j++)
                {
                    if(j<v-1)
                        t[i][j]=m[i][j];
                    else
                        t[i][j]=m[i][j+1];
                }
            }
            else
            {
                for(int j=0;j<t[i].length;j++)
                {
                    if(j<v-1)
                        t[i][j]=m[i+1][j];
                    else
                        t[i][j]=m[i+1][j+1];
                }
            }
        }

        //Debug
        //System.out.println("MatrixPlayer::confactorMatrix:");
        //showMatrix(data,3,3);
        //showMatrix(t,3,3);
        //

        return t;
    }

    //Show a 4*4 Matrix
    static public void showMatrix(double[][] m,int h,int v)
    {
        for(int i=0;i<h;i++)
        {
            for(int j=0;j<v;j++)
            {
                //when get the j = 3 ,then println
                if(j==h-1)
                {
                    //Debug
                    //System.out.println("i:"+i+"j:"+j+"    "+x[i][j]+"    ");
                    //
                    System.out.println("    "+m[i][j]+"    ");
                }
                else
                {
                    //Debug
                    //System.out.print("i:"+i+"j:"+j+"   "+x[i][j]+"    ");
                    //
                    System.out.print("    "+m[i][j]+"    ");
                }
            }
        }
    }

    //Debug Main
    public static void matrixDebug()
    {
        double[][] data = {
                {1,2,-1 },
                {3,1,0 },
                {-1,-1,-2 },
        };

        System.out.println("MatrixPlayer::matrixDebug:");
        //System.out.println("MatrixPlayer::originalMatrix");
        //showMatrix(data,3,3);
        //System.out.println("MatrixPlayer::reverseMatrix");
        //showMatrix(multiplyMatrix(reverseMatrix(data),data),3,3);
    }

    public static void showMatrix(float[] m){
        //1-4
        System.out.print(" Martix[1,1] "+ m[0]);
        System.out.print(" Martix[1,2] " +m[4]);
        System.out.print(" Martix[1,3] "+m[8]);
        System.out.println(" Martix[1,4] "+m[12]);

        System.out.print(" Martix[2,1] "+ m[1]);
        System.out.print(" Martix[2,2] " +m[5]);
        System.out.print(" Martix[2,3] "+m[9]);
        System.out.println(" Martix[2,4] "+m[13]);


        System.out.print(" Martix[3,1] "+ m[2]);
        System.out.print(" Martix[3,2] " +m[6]);
        System.out.print(" Martix[3,3] "+ m[10]);
        System.out.println(" Martix[3,4] "+m[14]);

        System.out.print(" Martix[4,1] "+ m[3]);
        System.out.print(" Martix[4,2] " +m[7]);
        System.out.print(" Martix[4,3] "+m[11]);
        System.out.println(" Martix[4,4] "+m[15]);
    }
}
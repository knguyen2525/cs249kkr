#include <iostream>
#include <string>
#include <fstream>
#include <algorithm>
#include <unordered_map>
#include <vector>
#include <sstream>
using namespace std;

bool isInside(const string & str, char c)
{
    return str.find(c) != string::npos;
}

int main()
{
    unordered_map<string,int> myMap;
    
    ifstream fin;
    fin.open("outputacm.txt");
    //ofstream myfile;
    //myfile.open("data2.csv");
    int n = 0;
    for(string line; getline(fin, line); )
    {
        if(line[1]=='c')
        {
            line.erase(0,2);
            if(isInside(line,',')) //if multiple authors
                {

                    stringstream ss(line);
                    
                    while( ss.good() )
                    {
                        string substr;
                        getline( ss, substr, ',' );
                        if(myMap.find(substr) == myMap.end())
                        {
                            myMap[substr]= ( n );
                            n++;
                        }
                    }

                }
            else //single author
                {
                    if(myMap.find(line)==myMap.end())
                        {
                            myMap[line]= n;
                            n++;
                        }
                }
        }
    }
    //for (unordered_map<string,int>::iterator it=myMap.begin(); it!=myMap.end(); ++it)
        //cout << it->first << " => " << it->second << '\n';
    fin.close();
    

    
    ifstream fin2;
    fin2.open("outputacm.txt");
    ofstream myfile;
    myfile.open("PublishedIn.csv");
    vector<int>myvector;
    int  count = 1;
    for(string line; getline(fin2, line); )
    {
        if(line[1]=='c')
        {
            cout << "Parsing paper " <<count <<endl;
            count++;
            line.erase(0,2);
            if(isInside(line,',')) //if multiple authors
            {
                
                stringstream ss(line);
                
                while( ss.good() )
                {
                    string substr;
                    getline( ss, substr, ',' );
                    if(myMap.find(substr) != myMap.end())
                    {
                        //cout << substr <<" found" <<endl;

                        unordered_map<string,int>::iterator got = myMap.find(substr);
                        myvector.push_back(got -> second);
                        //cout << "index "<< got -> second <<" changed " <<got -> first <<endl;
                    }
                }
                
            }
            else if(myMap.find(line)!=myMap.end())//single author
                {
                    //cout << line << " found" <<endl;
                    unordered_map<string,int>::iterator got = myMap.find(line);
                    //cout << got -> second<<endl;
                    myvector.push_back(got -> second);
                   
                    //cout << "***index "<< got -> second <<" changed " <<got -> first <<endl;
                }
            for(int i = 0; i <myvector.size();i++)
            {
                myfile << myvector[i]<< ",";
               
            }
            myfile << endl;
        myvector.clear();
        }
    }
}








{-# LANGUAGE GeneralizedNewtypeDeriving, UndecidableInstances, TypeSynonymInstances, FlexibleInstances #-}

module Names(
  Temp, Label, mkLabel, mkNamedTemp,
  MonadNameGen(..),
  NameGen, runNameGen,
  NameGenT, runNameGenT
  ) where

import Data.Monoid
import Control.Monad.State
import Control.Monad.Error
import Control.Monad.Identity
import Control.Monad.Trans.Reader (ReaderT)
import Control.Monad.Trans.Writer.Strict
import Text.PrettyPrint

data Temp = NamedTemp String | Temp Int 
   deriving (Eq, Ord)

instance Show Temp where
    show (Temp i) = "t" ++ show i
    show (NamedTemp s) = s

-- May lead to name clashes with 'nextTemp'
-- User must take care to avoid clashes.
mkNamedTemp :: String -> Temp
mkNamedTemp s = NamedTemp s

type Label = String

mkLabel :: String -> Label
mkLabel l | ('$' `elem` l) = 
              error $ "Label \"" ++ l ++ "\" contains reserver character '$'."
          | otherwise = 'L':l          
            
class Monad m => MonadNameGen m where
  nextTemp :: m Temp
  avoid :: [Temp] -> m ()
  nextLabel :: m Label
  
newtype NameGenT m a = NameGenT (StateT ([Temp], [Label]) m a) 
  deriving (Monad, MonadTrans)

type NameGen a = NameGenT Identity a 

runNameGen :: NameGenT Identity a -> a
runNameGen = runIdentity . runNameGenT 

instance (Monad m) => MonadNameGen (NameGenT m) where
  nextTemp = NameGenT $ do (t:ts, ls) <- get; put (ts, ls); return t
  avoid av = NameGenT $ do (ts, ls) <- get; put (filter (\t -> not (show t `elem` (map show av))) ts, ls); return () 
  nextLabel = NameGenT $ do (ts, l:ls) <- get; put (ts, ls); return l
  
runNameGenT :: (Monad m) => NameGenT m a -> m a
runNameGenT (NameGenT x) = 
   evalStateT x ([Temp i | i<-[0..]], ["L$" ++ (show i) | i <- [(0::Int)..]])
